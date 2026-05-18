package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.ElementTypes;

import java.util.List;

public class King extends Pawn {

    private static int solidersToCapture = 3;

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
    public boolean shouldDie() {
        TablutBoard board = (TablutBoard)getContainer();
        int ennemyNeighbors = 0;
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int[] position = {getXInBoard() + vect[0], getYInBoard() + vect[1]};
            if (board.isEmptyAt(position[0], position[1])) {
                continue;
            }
            Pawn pawn = (Pawn)board.getElement(position[0], position[1]);
            if (pawn.getTeam() != getTeam()) {
                ennemyNeighbors ++;
            }
        }
        return ennemyNeighbors >= solidersToCapture;
    }
}
