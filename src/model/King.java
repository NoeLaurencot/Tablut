package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.ElementTypes;

import java.util.List;

public class King extends Pawn {

    public King(GameStageModel gameStageModel) {
        super(Team.SWEDISH, gameStageModel);
        ElementTypes.register("king", 200);
    }

    public King(double x, double y, GameStageModel gameStageModel) {
        super(x, y, Team.SWEDISH, gameStageModel);
        ElementTypes.register("king", 200);
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
    public void checkShouldDie(List<GameElement>[][] pawns) {

    }
}
