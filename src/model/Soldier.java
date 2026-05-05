package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

import java.util.List;

public class Soldier extends Pawn {

    public Soldier(Team team, GameStageModel gameStageModel) {
        super(team, gameStageModel);
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
