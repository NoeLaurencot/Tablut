package control;

import boardifier.control.*;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import model.*;

import java.util.ArrayList;

public class TablutController extends Controller {

    boolean firstPlayer;
    private Move lastMove;

    public TablutController(Model model, View view) {
        super(model, view);
        setControlAction(new TablutControllerAction(model, view, this));
        setControlMouse(new ControllerTablutMouse(model, view, this));
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
            final int COOLDOWN = 500;

            Decider decider = getDeciderType(stageModel.getMode(), stageModel.getAIModesUsed());

            long timestamp = System.nanoTime();

            ActionList actionList = decider.decide();

            ActionPlayer play = new ActionPlayer(model, this, actionList);

            waitMinimumCooldown(COOLDOWN, timestamp);

            play.start();

            if (decider instanceof TablutMinMaxDecider) {
                bestMove = ((TablutMinMaxDecider) decider).bestMove;
            } else if (decider instanceof TablutSimpleDecider) {
                bestMove = ((TablutSimpleDecider) decider).bestMove;
            }
            this.lastMove = bestMove;
        } else {
            Logger.debug("PLAYER PLAYS");
        }
    }

    // TODO: remake this function so it thinks there is a pawn at dest, then put actions in the actionList
    public void addAfterPlayActions(int destRow, int destCol, ActionList actionList) {
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        TablutStageModel gameStage = (TablutStageModel) model.getGameStage();
        TablutBoard board = gameStage.getBoard();
        Pawn movedPawn = (Pawn) board.getLastElement(destRow, destCol);

        for (int[] dir : directions) {
            int nextRow = destRow + dir[0];
            int nextCol = destCol + dir[1];

            if (board.outOfBounds(new Coordinate(nextRow, nextCol))) continue;
            if (!board.isPawnAt(nextRow, nextCol)) continue;

            Pawn enemyPawn = (Pawn) board.getLastElement(nextRow, nextCol);

            if (enemyPawn.getTeam() == movedPawn.getTeam()) continue;

            // A capture needs something friendly on the other side
            int beyondRow = nextRow + dir[0];
            int beyondCol = nextCol + dir[1];

            if (board.outOfBounds(new Coordinate(beyondRow, beyondCol))) continue;

            boolean hasFriendlyPawnBeyond = board.getElement(beyondRow, beyondCol) instanceof Pawn
                    && ((Pawn) board.getLastElement(beyondRow, beyondCol)).getTeam() == movedPawn.getTeam();
            boolean hasFortressBeyond = board.isFortressAt(beyondRow, beyondCol);

            if ((hasFriendlyPawnBeyond || hasFortressBeyond) && enemyPawn.shouldDie()) {
                capturePawn(enemyPawn);
            }
        }
    }

    private void capturePawn(Pawn pawn) {
        ActionList actions = ActionFactory.generateRemoveFromContainer(model, pawn);
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
        pawn.setVisible(false);
    }

    public void setLastMove(Move move) {
        this.lastMove = move;
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
        timeElapsed *= 10e-3;

        if (timeElapsed < cooldown) {
            try {
                Thread.sleep(timeElapsed + (cooldown - timeElapsed));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
