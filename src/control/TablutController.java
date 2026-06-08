package control;

import boardifier.control.*;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import model.*;

import javax.swing.*;
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

        afterPlay(lastMove.destX, lastMove.destY);
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
            final int COOLDOWN = 300;

            Decider decider = getDeciderType(stageModel.getMode(), stageModel.getAIModesUsed());

            long timestamp = System.nanoTime();

            ActionPlayer play = new ActionPlayer(model, this, decider, null);

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

    /**
     * Checks for captures
     */
    public void afterPlay(int destRow, int destCol) {
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        TablutStageModel gameStage = (TablutStageModel) model.getGameStage();
        TablutBoard board = gameStage.getBoard();
        Pawn pawn = (Pawn) board.getLastElement(destRow, destCol);

        for (int[] vect : directions) {

            // first check if the next cell is an ennemy pawn
            int[] position = {destRow + vect[0], destCol + vect[1]};

            if (board.outOfBounds(new Coordinate(position[0], position[1])))
                continue;

            if (!board.isPawnAt(position[0], position[1]))
                continue;
            
            Pawn ennemyPawn = (Pawn) board.getLastElement(position[0], position[1]);
            if (ennemyPawn.getTeam() == pawn.getTeam())
                continue;

            // checks the further cell if it is a ally pawn (capture)
            position[0] += vect[0];
            position[1] += vect[1];

            if (position[0] < 0 || position[0] > 8) continue;
            if (position[1] < 0 || position[1] > 8) continue;

            if (board.isPawnAt(position[0], position[1]) == board.isFortressAt(position[0], position[1]))
                continue;

            System.out.println(position[0] + " " + position[1]);
            Pawn otherPawn = (Pawn) board.getLastElement(position[0], position[1]);


            if ((board.isFortressAt(position[0], position[1]) || otherPawn.getTeam() == pawn.getTeam()) && ennemyPawn.shouldDie()) {
                ActionList actions = ActionFactory.generateRemoveFromContainer(model, ennemyPawn);
                ActionPlayer play = new ActionPlayer(model, this, actions);
                play.start();
                ennemyPawn.setVisible(false);
            }
        }
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
                case SIMPLE_EVAL -> new TablutSimpleDecider(this.model, this);
            }
        } else {
            if (stageModel.getPlayerName().getText().equals("swedish computer")) {
                switch (aiModes.get(0)) { // first AI is swedish AI
                    case MINMAX -> decider = new TablutMinMaxDecider(this.model, this);
                    case SIMPLE_EVAL -> new TablutSimpleDecider(this.model, this);
                }
            } else {
                switch (aiModes.get(1)) { // second AI is muscovite AI
                    case MINMAX -> decider = new TablutMinMaxDecider(this.model, this);
                    case SIMPLE_EVAL -> new TablutSimpleDecider(this.model, this);
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

        if (timeElapsed < cooldown) {
            try {
                Thread.sleep(timeElapsed + (cooldown - timeElapsed));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
