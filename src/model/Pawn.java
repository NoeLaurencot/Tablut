package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

/**
 * A basic pawn element, with only 2 fixed parameters : number and color
 * There are no setters because the state of a Hole pawn is fixed.
 */
public abstract class Pawn extends GameElement {

    private PawnTeam team;

    public Pawn(PawnTeam team, GameStageModel gameStageModel) {
        super(gameStageModel);
        /*
        TO FULFILL:
            - register a new type of element for the pawns
            - initialize attributes
         */
    }

    public abstract int[][] getLegalMoves();
}

enum PawnTeam {
    MUSCOVITES,SWEDISH
}
