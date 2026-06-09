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

public class TablutController extends Controller {

    boolean firstPlayer;

    public TablutController(Model model, View view) {
        super(model, view);
        setControlAction(new TablutControllerAction(model, view, this));
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
        if (p.getType() == Player.COMPUTER) {
            Logger.debug("COMPUTER PLAYS");

            Move bestMove = null;
            final int COOLDOWN = 1000; // Don't put more than 3 seconds or OS thinks app doesn't respond

            Decider decider = getDeciderType(stageModel.getMode(), stageModel.getAIModesUsed());

            long timestamp = System.nanoTime();

            ActionList actionList = decider.decide();

            if (decider instanceof TablutMinMaxDecider) {
                bestMove = ((TablutMinMaxDecider) decider).bestMove;
            } else if (decider instanceof TablutSimpleDecider) {
                bestMove = ((TablutSimpleDecider) decider).bestMove;
            }

            addAfterPlayActions(bestMove, actionList);

            ActionPlayer play = new ActionPlayer(model, this, actionList);

            waitMinimumCooldown(COOLDOWN, timestamp);

            play.start();
        } else {
            Logger.debug("PLAYER PLAYS");
        }
    }

    public void addAfterPlayActions(Move move, ActionList actionList) {
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        TablutStageModel gameStage = (TablutStageModel) model.getGameStage();
        TablutBoard board = gameStage.getBoard();
        Pawn movedPawn = (Pawn) board.getLastElement(move.sourceX, move.sourceY);

        System.out.println(board.getLastElement(move.sourceX, move.sourceY) + " in " + move.destX + " " + move.destY + " surrounded by:");

        for (int[] dir : directions) {
            int nextRow = move.destX + dir[0];
            int nextCol = move.destY + dir[1];

            if (nextRow == move.sourceX && nextCol == move.sourceY) continue;

            if (board.outOfBounds(new Coordinate(nextRow, nextCol))) continue;
            if (!board.isPawnAt(nextRow, nextCol)) continue;

            Pawn enemyPawn = (Pawn) board.getLastElement(nextRow, nextCol);
            List<GameElement> list = board.getElements(nextRow, nextCol);

//            System.out.println("\t" + enemyPawn);
            System.out.println("\t" + list);

            if (enemyPawn.getTeam() == movedPawn.getTeam()) continue;

            // A capture needs something friendly on the other side
            int beyondRow = nextRow + dir[0];
            int beyondCol = nextCol + dir[1];

            if (board.outOfBounds(new Coordinate(beyondRow, beyondCol))) continue;

            boolean hasFriendlyPawnBeyond = board.getElement(beyondRow, beyondCol) instanceof Pawn
                    && ((Pawn) board.getLastElement(beyondRow, beyondCol)).getTeam() == movedPawn.getTeam();
            boolean hasFortressBeyond = board.isFortressAt(beyondRow, beyondCol);

            if ((hasFriendlyPawnBeyond || hasFortressBeyond) && enemyPawn.wouldBeSurroundedIfEnemyAt(new Coordinate(move.destX, move.destY))) {
                actionList.addSingleAction(new RemoveFromContainerAction(model, enemyPawn));
                enemyPawn.setVisible(false);
            }
        }
        System.out.println();
    }

    public Decider getDeciderType(int mode, ArrayList<AIMode> aiModes) {
        TablutStageModel stageModel = (TablutStageModel) model.getGameStage();
        Decider decider = new TablutMinMaxDecider(this.model, this); // defaults to minmax

        if (mode == 1 || mode == 2) {
            switch (aiModes.getFirst()) {
                case MINMAX -> decider = new TablutMinMaxDecider(this.model, this);
                case SIMPLE_EVAL -> decider = new TablutSimpleDecider(this.model, this);
            }
        } else {
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
     * Makes the AI turn take at least a certain amount of time before ending turn
     * @param cooldown the minimum time before the turn ends
     * @param timestamp the timestamp taken before deciding best move
     */
    public void waitMinimumCooldown(int cooldown, long timestamp) {

        long timeElapsed = System.nanoTime() - timestamp;

        // from nanosecond to milisecond
        timeElapsed *= 10e-6;

        if (timeElapsed < cooldown) {
            try {
                Thread.sleep(cooldown - timeElapsed);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
