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
        List<Coordinate> lst = new ArrayList<>();
        TablutBoard board = (TablutBoard) getContainer();

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int inc = 1;
            int[] position = {getXInBoard() + vect[0] * inc, getYInBoard() + vect[1] * inc};
//            System.out.println(position[0] + " " + position[1]);

            while (position[0] >= 0 && position[0] < 9
                    && position[1] >= 0 && position[1] < 9
                    && board.isEmptyAt(position[0], position[1])
                    && !board.getGameStage().isFortressAt(position[0], position[1])) {

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

    public boolean wouldDie(Coordinate attackerCoord) {
        TablutBoard board = (TablutBoard) getContainer();
        TablutStageModel model = board.getGameStage();
        int solidersToCapture = 2;
        int ennemyNeighbors = 0;

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int[] position = {getXInBoard() + vect[0], getYInBoard() + vect[1]};

            if (position[0] == attackerCoord.getX() && position[1] == attackerCoord.getY())
                ennemyNeighbors++;

            if (board.isEmptyAt(position[0], position[1])) {
                continue;
            }
            if (model.isFortressAt(position[0], position[1])) {
                ennemyNeighbors++;
            }
            Pawn pawn = (Pawn) board.getElement(position[0], position[1]);

            if (pawn.getTeam() != getTeam()) {
                ennemyNeighbors++;
            }
        }
        return ennemyNeighbors >= solidersToCapture;
    }

    @Override
    public boolean isSurrounded() {
        TablutBoard board = ((TablutStageModel) gameStageModel).getBoard();
        int x = getXInBoard();
        int y = getYInBoard();
        Set<Integer> seenX = new HashSet<>();
        Set<Integer> seenY = new HashSet<>();

        for (Coordinate coord : board.getAdjacentEnemyPawnsCoords(new Coordinate(x, y))) {
            // check if x or y is the same twice, so in a line
            if (!seenX.add(coord.getX()) || !seenY.add(coord.getY())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean wouldBeSurrounded(Coordinate coord) {
        TablutBoard board = ((TablutStageModel) gameStageModel).getBoard();
        int x = coord.getX();
        int y = coord.getY();
        Set<Integer> seenX = new HashSet<>();
        Set<Integer> seenY = new HashSet<>();

        for (Coordinate coordAdj : board.getAdjacentEnemyPawnsCoords(new Coordinate(x, y))) {
            // check if x or y is the same tiwce, so in a line
            if (!seenX.add(coordAdj.getX()) || !seenY.add(coordAdj.getY())) {
                return true;
            }
        }

        return false;
    }
}
