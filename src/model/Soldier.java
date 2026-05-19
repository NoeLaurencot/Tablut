package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.ElementTypes;


import java.util.List;
import java.util.ArrayList;
import java.awt.*;

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
    public List<Point> getLegalMoves() {
        List<Point> lst = new ArrayList<>();
        TablutBoard board = (TablutBoard)getContainer();

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int inc = 1;
            int[] position = {getXInBoard() + vect[0]*inc, getYInBoard() + vect[1]*inc};
            System.out.println(position[0] + " " + position[1]);

            while (position[0] >= 0 && position[0] < 9
                && position[1] >= 0 && position[1] < 9
                && board.isEmptyAt(position[0], position[1])
                && !board.getGameStage().isFortressAt(position[0], position[1])) {
                
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
        return true;
    }
}
