package control;

import boardifier.control.*;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.model.action.RemoveFromContainerAction;
import boardifier.view.View;
import model.*;

import java.util.ArrayList;
import java.util.List;
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

        // use the default method to compute next player
        model.setNextPlayer();
        // get the new player
        Player p = model.getCurrentPlayer();
        // change the text of the TextElement
        TablutStageModel stageModel = (TablutStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());
        addLastMoveToHistory();
        if (p.getType() == Player.COMPUTER) {
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
     * @param move
     * @param actionList
     */
    public void addAfterPlayActions(Move move, ActionList actionList) {
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
            // TODO: see if better way to check player type
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

    /**
     * Execute the function that searches the best move and gives the ActionList generated in a certain minimum amount of time
     * @param cooldown the minimum time required before playing
     * @param function the function that searches for the best move
     * @param moves the move list
     * @return the ActionList containing the bestMove
     */
    public static ActionList getActionListAfterCooldown(int cooldown, Function<List<Move>, ActionList> function, List<Move> moves) {


        long timestamp = System.currentTimeMillis();

        ActionList actions = function.apply(moves);

        long timeElapsed = System.currentTimeMillis() - timestamp;

        if (timeElapsed < cooldown) {
            try {
                Thread.sleep(cooldown - timeElapsed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return actions;
    }

}
