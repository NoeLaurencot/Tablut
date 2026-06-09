package model;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Logger;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.action.ActionList;
import boardifier.model.animation.Animation;
import boardifier.model.animation.AnimationStep;

import java.util.List;

/**
 * A basic pawn element, with only 2 fixed parameters : number and color
 * There are no setters because the state of a pawn is fixed.
 */
public abstract class Pawn extends GameElement {

    private Team team;

    public Pawn(Team team, GameStageModel gameStageModel) {
        super(gameStageModel);

        this.team = team;
    }

    public Pawn(double x, double y, Team team, GameStageModel gameStageModel) {
        super(gameStageModel);
        setLocation(x, y);
        this.team = team;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getXInBoard() {
        TablutBoard board = ((TablutStageModel) getGameStage()).getBoard();

        for (int i = 0; i < board.getNbRows(); i++) {
            for (int j = 0; j < board.getNbCols(); j++) {
                if (board.getLastElement(i, j) == this) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getYInBoard() {
        TablutBoard board = ((TablutStageModel) getGameStage()).getBoard();

        for (int i = 0; i < board.getNbRows(); i++) {
            for (int j = 0; j < board.getNbCols(); j++) {
                if (board.getLastElement(i, j) == this) {
                    return j;
                }
            }
        }
        return -1;
    }

    public abstract List<Coordinate> getLegalMoves();

    protected abstract boolean checkIsMoveValid();

    public abstract boolean shouldDie();

    public ActionList move(int x, int y, Controller controller) {
        return ActionFactory.generateMoveWithinContainer(controller, this.getModel(), this, x, y);
    }

    public ActionList die(Pawn pawn) {
        return ActionFactory.generateRemoveFromContainer(this.getModel(), this);
    }

    public abstract boolean isSurrounded();

    public void update() {
        // if must be animated, move the pawn
        if (animation != null) {
            AnimationStep step = animation.next();
            if (step == null) {
                animation = null;
            }
            else if (step == Animation.NOPStep) {
                Logger.debug("nothing to do", this);
            }
            else {
                Logger.debug("move animation", this);
                setLocation(step.getInt(0), step.getInt(1));
            }
        }
    }


    /**
     * checks if would be surrounded, and thus captured, if there was an enemy pawn at coord
     * @param coord The coordinates the enemy pawn would be at
     * @return true if would be surrounded
     */
    public abstract boolean wouldBeSurroundedIfEnemyAt(Coordinate coord);

    @Override
    public String toString() {
        char[] charTable = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        return (this.team + " "  + this.getClass().getSimpleName() + " " + charTable[getYInBoard()] + (getXInBoard()+1));
    }
}
