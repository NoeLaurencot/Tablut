package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.ElementTypes;


import java.util.List;

public class Soldier extends Pawn {

    public Soldier(Team team, GameStageModel gameStageModel) {
        super(team, gameStageModel);
        ElementTypes.register("soldier", 100);
    }

    public Soldier(double x, double y, Team team, GameStageModel gameStageModel) {
        super(x, y, team, gameStageModel);
        ElementTypes.register("soldier", 100);
    }


    @Override
    public int[][] getLegalMoves() {
        return new int[0][];
    }

    @Override
    protected boolean checkIsMoveValid() {
        return false;
    }

    @Override
    public void checkIfDie(List<GameElement>[][] pawns) {

    }
}
