package model;

import boardifier.model.GameStageModel;
import boardifier.model.ElementTypes;


import java.util.List;
import java.util.ArrayList;

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

    public boolean isCellCapturableNextMove(Coordinate startCoord, Coordinate coord, Team team) {
        TablutBoard board = (TablutBoard) getContainer();
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

        if (this.countEnemyNieghbors(this.getNeighbors(coord.getX(), coord.getY()), team) > 0) {
            for (int[] vect : directions) {
                int[] position = {coord.getX() + vect[0], coord.getY() + vect[1]};

                if (position[0] < 0 || position[0] > 8) continue;
                if (position[1] < 0 || position[1] > 8) continue;

                if (!board.isEmptyAt(position[0],position[1]) && ((Pawn) board.getElement(position[0],position[1])).getTeam() != team) {
                    position[0] -= vect[0]*2;
                    position[1] -= vect[1]*2;

                    if (position[0] < 0 || position[0] > 8) continue;
                    if (position[1] < 0 || position[1] > 8) continue;

                    if (!board.isEmptyAt(position[0],position[1])) continue;

                    for (int[] subVect : directions) {
                        int[] subPosition = {position[0] + subVect[0], position[1] + subVect[1]};
                        // skip the direction where the starting point is
                        if (subVect[0] == vect[0] && subVect[1] == vect[1]) continue;

                        while (subPosition[0] >= 0 && subPosition[0] <= 8 && subPosition[1] >= 0 && subPosition[1] <= 8) {
                            if (!board.isEmptyAt(subPosition[0], subPosition[1])) {
                                if (subPosition[0] == startCoord.getX() && subPosition[1] == startCoord.getY()) {
                                    subPosition[0] += subVect[0];
                                    subPosition[1] += subVect[1];
                                    continue;
                                }
                                if (((Pawn) board.getElement(subPosition[0], subPosition[1])).getTeam() != team) {
                                    return true;
                                }
                                break;
                            }
                            subPosition[0] += subVect[0];
                            subPosition[1] += subVect[1];
                        }
                    }
                }


            }
        }
        return false;
    }
}
