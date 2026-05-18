package model;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.action.ActionList;

import java.util.List;

/**
 * A basic pawn element, with only 2 fixed parameters : number and color
 * There are no setters because the state of a Hole pawn is fixed.
 */
public abstract class Pawn extends GameElement {

    private Team team;

    public Pawn(Team team, GameStageModel gameStageModel) {
        super(gameStageModel);
        /*
        TO FULFILL (done?):
            - register a new type of element for the pawns
            - initialize attributes
         */

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

    protected int getXInBoard() {
        TablutBoard board = ((TablutStageModel)getGameStage()).getBoard();
        
        for (int i = 0; i < board.getNbRows(); i++) {
            for (int j = 0; j < board.getNbCols(); j++) {
                if (board.getElement(i, j) == this) {
                    return i;
                }
            }
        } 
        return -1;
    }

    protected int getYInBoard() {
        TablutBoard board = ((TablutStageModel)getGameStage()).getBoard();
        
        for (int i = 0; i < board.getNbRows(); i++) {
            for (int j = 0; j < board.getNbCols(); j++) {
                if (board.getElement(i, j) == this) {
                    return j;
                }
            }
        } 
        return -1;
    }

    public abstract int[][] getLegalMoves();

    protected abstract boolean checkIsMoveValid();

    public abstract boolean shouldDie();

    public ActionList move(int x, int y) {
        return ActionFactory.generateMoveWithinContainer(this.getModel(), this, x, y);
    }

    public ActionList die(Pawn pawn) {
        return ActionFactory.generateRemoveFromContainer(this.getModel(), this);
    }
}
