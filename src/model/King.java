package model;

import boardifier.model.GameStageModel;
import boardifier.model.ElementTypes;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class King extends Pawn {

    private static int solidersToCapture = 4;

    public King(GameStageModel gameStageModel) {
        super(Team.SWEDISH, gameStageModel);
        ElementTypes.register("king", 200);
    }

    public King(double x, double y, GameStageModel gameStageModel) {
        super(x, y, Team.SWEDISH, gameStageModel);
        ElementTypes.register("king", 200);
    }


    @Override
    public List<Coordinate> getLegalMoves() {
        List<Coordinate> lst = new ArrayList<>();
        TablutBoard board = (TablutBoard) getContainer();

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int inc = 1;
            int[] position = {getXInBoard() + vect[0] * inc, getYInBoard() + vect[1] * inc};
            //System.out.println(position[0] + " " + position[1]);

            if (((TablutStageModel) this.gameStageModel).getKingMoveMode() == KingMoveMode.LIMITLESS) {
                while (position[0] >= 0 && position[0] < 9
                        && position[1] >= 0 && position[1] < 9
                        && board.isEmptyAt(position[0], position[1])) {

                    lst.add(new Coordinate(position[0], position[1]));
                    inc++;
                    position[0] = getXInBoard() + vect[0] * inc;
                    position[1] = getYInBoard() + vect[1] * inc;
                }
            } else {
                if (position[0] >= 0 && position[0] < 9 && position[1] >= 0 && position[1] < 9 && board.isEmptyAt(position[0], position[1])) {
                    lst.add(new Coordinate(position[0], position[1]));
                }
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
        TablutBoard board = (TablutBoard) getContainer();
        TablutStageModel model = board.getGameStage();

        int ennemyNeighbors = 0;
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int[] position = {getXInBoard() + vect[0], getYInBoard() + vect[1]};

            if (board.isEmptyAt(position[0], position[1])) {
                continue;
            }
            if (model.isFortressAt(position[0], position[1]) && position[0] == 4 && position[1] == 4) {
                ennemyNeighbors++;
            }
            Pawn pawn = (Pawn) board.getElement(position[0], position[1]);
//            System.out.println(position[0] + " " + position[1]);

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
            // check if x and y is the same tiwce, so in a cross surrounding the king
            if (!seenX.add(coord.getX()) && !seenY.add(coord.getY())) {
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
            // check if x and y is the same tiwce, so in a cross surrounding the king
            if (!seenX.add(coordAdj.getX()) && !seenY.add(coordAdj.getY())) {
                return true;
            }
        }

        return false;
    }
}
