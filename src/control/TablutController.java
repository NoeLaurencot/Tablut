package control;

import boardifier.control.*;
import boardifier.model.GameElement;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.model.action.RemoveFromContainerAction;
import boardifier.view.View;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;
import model.*;
import view.TablutView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TablutController extends Controller {

    private Move lastMove;
    boolean firstPlayer;

    public TablutController(Model model, View view) {
        super(model, view);
        setControlAction(new ControllerTablutAction(model, view, this));
        setControlMouse(new ControllerTablutMouse(model, view, this));
        setControlKey(new ControllerTablutKey(model, view, this));
        firstPlayer = true;
    }

    public void endOfTurn() {

        // use the default method to compute next player (only if cheat disabled)
        if (!TablutOptionModel.isMoveAnyPawn())
            model.setNextPlayer();

        // get the new player
        Player p = model.getCurrentPlayer();
        // change the text of the TextElement
        TablutStageModel stageModel = (TablutStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());
        addLastMoveToHistory();
        if (p.getType() == Player.COMPUTER && !TablutOptionModel.isMoveAnyPawn()) {
            Logger.debug("COMPUTER PLAYS");

            Decider decider = getDeciderType(TablutOptionModel.getGamemode(), TablutOptionModel.getAiMode());

            ActionPlayer play = new ActionPlayer(model, this, decider, null);

            play.start();

        } else {
            Logger.debug("PLAYER PLAYS");
        }
    }

    /**
     * Adds an after play action in the actionList
     *
     * @param move
     * @param actionList
     */
    public void addAfterPlayActions(Move move, ActionList actionList) {
        if (TablutOptionModel.isCapturesDisabled()) {
            return;
        }

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        TablutStageModel gameStage = (TablutStageModel) model.getGameStage();
        TablutBoard board = gameStage.getBoard();
        Pawn movedPawn = (Pawn) board.getLastElement(move.sourceX, move.sourceY);

        for (int[] dir : directions) {
            int nextRow = move.destX + dir[0];
            int nextCol = move.destY + dir[1];

            if (nextRow == move.sourceX && nextCol == move.sourceY) continue;

            if (board.outOfBounds(new Coordinate(nextRow, nextCol))) continue;
            if (!board.isPawnAt(nextRow, nextCol)) continue;

            Pawn enemyPawn = (Pawn) board.getLastElement(nextRow, nextCol);

            if (enemyPawn.getTeam() == movedPawn.getTeam()) continue;

            // A capture needs something friendly on the other side
            int beyondRow = nextRow + dir[0];
            int beyondCol = nextCol + dir[1];

            if (board.outOfBounds(new Coordinate(beyondRow, beyondCol))) continue;

            GameElement pawnBeyond = board.getLastElement(beyondRow, beyondCol);

            boolean hasFriendlyPawnBeyond = pawnBeyond instanceof Pawn
                    && ((Pawn) pawnBeyond).getTeam() == movedPawn.getTeam();
            boolean hasFortressBeyond = board.isFortressAt(beyondRow, beyondCol);

            if ((hasFriendlyPawnBeyond || hasFortressBeyond) && enemyPawn.wouldBeSurroundedIfEnemyAt(new Coordinate(move.destX, move.destY))) {
                actionList.addSingleAction(new RemoveFromContainerAction(model, enemyPawn));
                enemyPawn.setVisible(false);
            }
        }
    }

    public void setLastMove(Move move) {
        this.lastMove = move;
    }

    public void addLastMoveToHistory() {
        TablutStageModel gameStage = (TablutStageModel) model.getGameStage();
        if (lastMove == null) return;

        MoveElement moveElement = new MoveElement(lastMove, gameStage);
        gameStage.addMoveInHistory(moveElement);
    }

    public Decider getDeciderType(int mode, ArrayList<AIMode> aiModes) {
        TablutStageModel stageModel = (TablutStageModel) model.getGameStage();
        Decider decider = new TablutSimpleDecider(this.model, this); // defaults to simple eval

        if (mode == 1 || mode == 2) {
            switch (aiModes.getFirst()) {
                case MINMAX -> decider = new TablutMinMaxDecider(this.model, this);
                case SIMPLE_EVAL -> decider = new TablutSimpleDecider(this.model, this);
            }
        } else if (mode == 3) {
            if (stageModel.getPlayerName().getText().equals("swedish computer")) {
                switch (aiModes.get(0)) { // first AI is swedish AI
                    case MINMAX -> decider = new TablutMinMaxDecider(this.model, this);
                    case SIMPLE_EVAL -> decider = new TablutSimpleDecider(this.model, this);
                }
            } else {
                switch (aiModes.get(1)) { // second AI is muscovite AI
                    case MINMAX -> decider = new TablutMinMaxDecider(this.model, this);
                    case SIMPLE_EVAL -> decider = new TablutSimpleDecider(this.model, this);
                }
            }
        }

        return decider;
    }

    public static void waitCooldown(int cooldown, long timestamp) {

        long timeElapsed = System.currentTimeMillis() - timestamp;

        if (timeElapsed < cooldown) {
            try {
                Thread.sleep(cooldown - timeElapsed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void endGame() {
        //Logger.traceln("END THE GAME");

        String message = "";
        if (model.getIdWinner() != -1) {
            Team winTeam;
            TablutBoard board = ((TablutStageModel) model.getGameStage()).getBoard();
            Player winPlayer = model.getPlayers().get((model.getIdWinner()));
            Player lostPlayer = model.getPlayers().get((model.getIdWinner() + 1) % model.getPlayers().size());

            if (model.getPlayers().get(model.getIdWinner()).getName().contains("swedish"))
                winTeam = Team.SWEDISH;
            else
                winTeam = Team.MUSCOVITES;

            if (board.isStalemate(winTeam.oppositeTeam())) {

                message = lostPlayer.getName() + " was put in a stalemate, " + winPlayer.getName() + " wins";
            } else {
                message = winPlayer.getName() + " wins";
            }


        } else {
            message = "Draw game";
        }
        // disable all events
        model.setCaptureEvents(false);
        // create a dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        // remove the frame around the dialog
        alert.initStyle(StageStyle.UNDECORATED);
        // make it a children of the main game window => it appears centered
        alert.initOwner(view.getStage());
        // set the message displayed
        alert.setHeaderText(message);
        // define new ButtonType to fit with our needs => one type is for Quit, one for New Game
        ButtonType quit = new ButtonType("Quit");
        ButtonType settings = new ButtonType("Settings");
        ButtonType newGame = new ButtonType("New Game");
        // remove default ButtonTypes
        alert.getButtonTypes().clear();
        // add the new ones
        alert.getButtonTypes().addAll(quit, settings, newGame);
        // show the dialog and wait for the result
        Optional<ButtonType> option = alert.showAndWait();
        // check if result is quit
        if (option.get() == quit) {
            System.exit(0);
        }
        // check if result is new game
        else if (option.get() == newGame) {
            try {
                startGame();
                if (TablutOptionModel.getGamemode() >= 2) {
                    model.setNextPlayer();
                    this.endOfTurn();
                }
            } catch (GameException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        // open settings
        else if (option.get() == settings) {
            view.resetView();
            ((TablutView) view).showOptionMenu();
        }
        // abnormal case :-)
        else {
            System.err.println("Abnormal case: dialog closed with not choice");
            System.exit(1);
        }
    }

    public void resetHistory() {
        ((TablutStageModel) model.getGameStage()).getMoveHistory().reset();
    }
}
