package model;

import boardifier.control.ActionFactory;
import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

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

    public abstract int[][] getLegalMoves();

    public abstract void checkIfDie(List<GameElement>[][] pawns);

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void die(List<GameElement>[][] pawns) {
        for (int i = 0; i < pawns.length; i++) {
            for (int j = 0; j < pawns[i].length; j++) {
                for (int k = 0; k < pawns[i][j].size(); k++) {
                    ActionFactory.generateRemoveFromContainer(this.getModel(), this);
                }
            }
        }
    }


}
