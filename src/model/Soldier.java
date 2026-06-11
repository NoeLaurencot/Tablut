package model;

import boardifier.model.GameStageModel;
import boardifier.model.ElementTypes;


import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

/**
 * A soldier Pawn regroup the ways soldier can die ,and move
 */
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
    public List<Coordinate> getLegalMoves() {
        TablutBoard board = (TablutBoard) getContainer();
        List<Coordinate> lst = new ArrayList<>();

        if (TablutOptionModel.isMoveAnywhere()) {
            for (int i = 0; i < board.getNbRows(); i++) {
                for (int j = 0; j < board.getNbCols(); j++) {
                    if (board.isEmptyAt(i, j)) {
                        lst.add(new Coordinate(i , j));
                    }
                }
            }
            return lst;
        }

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int inc = 1;
            int[] position = {getXInBoard() + vect[0] * inc, getYInBoard() + vect[1] * inc};
//            System.out.println(position[0] + " " + position[1]);

            while (position[0] >= 0 && position[0] < 9
                    && position[1] >= 0 && position[1] < 9
                    && board.isEmptyAt(position[0], position[1])) {

                lst.add(new Coordinate(position[0], position[1]));
                inc++;
                position[0] = getXInBoard() + vect[0] * inc;
                position[1] = getYInBoard() + vect[1] * inc;
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

    @Override
    public boolean isSurrounded() {
        TablutBoard board = (TablutBoard) getContainer();
        int x = getXInBoard();
        int y = getYInBoard();
        Team team = this.getTeam();

        boolean hasLeft = false;
        boolean hasRight = false;
        boolean hasTop = false;
        boolean hasBottom = false;

        for (Coordinate coord : board.getAdjacentEnemyPawnsCoords(new Coordinate(x, y), team)) {
            if (coord.getX() < x)
                hasLeft = true;
            else if (coord.getX() > x)
                hasRight = true;
            else if (coord.getY() < y)
                hasTop = true;
            else if (coord.getY() > y)
                hasBottom = true;

            if ((hasLeft && hasRight) || (hasTop && hasBottom))
                return true;
        }

        return false;
    }

    @Override
    public boolean wouldBeSurroundedIfEnemyAt(Coordinate coord) {
        TablutBoard board = (TablutBoard) getContainer();
        int x = getXInBoard();
        int y = getYInBoard();

        Team team = this.getTeam();

        for (Coordinate coordAdj : board.getAdjacentEnemyPawnsCoords(new Coordinate(x, y), team)) {
            if (coord.getX() == coordAdj.getX() || coord.getY() == coordAdj.getY())
                return true;
        }

        return false;
    }
}
