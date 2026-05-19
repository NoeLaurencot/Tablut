package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.ElementTypes;

import java.util.List;
import java.util.ArrayList;
import java.awt.*;

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
    public List<Point> getLegalMoves() {
        List<Point> lst = new ArrayList<>();
        TablutBoard board = (TablutBoard)getContainer();

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int inc = 1;
            int[] position = {getXInBoard() + vect[0]*inc, getYInBoard() + vect[1]*inc};
            //System.out.println(position[0] + " " + position[1]);

            while (position[0] >= 0 && position[0] < 9
                && position[1] >= 0 && position[1] < 9
                && board.isEmptyAt(position[0], position[1])) {
                
                lst.add(new Point(position[0], position[1]));
                inc++;
                position[0] = getXInBoard() + vect[0]*inc; 
                position[1] = getYInBoard() + vect[1]*inc;
            }
        }

        return lst;
    }

    @Override
    protected boolean checkIsMoveValid() {
        return false;
    }

    @Override
    public boolean shouldDie() {
        TablutBoard board = (TablutBoard)getContainer();
        TablutStageModel model = board.getGameStage();

        int ennemyNeighbors = 0;
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int[] position = {getXInBoard() + vect[0], getYInBoard() + vect[1]};
            if (model.isFortressAt(position[0], position[1])) {
                ennemyNeighbors ++;
            }

            if (board.isEmptyAt(position[0], position[1])) {
                continue;
            }
            Pawn pawn = (Pawn)board.getElement(position[0], position[1]);
            System.out.println(position[0] + " " + position[1]);
        
            if (pawn.getTeam() != getTeam()) {
                ennemyNeighbors ++;
            }
        }
        return ennemyNeighbors >= solidersToCapture;
    }
}
