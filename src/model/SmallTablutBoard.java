package model;

import java.util.ArrayList;
import java.util.List;

public class SmallTablutBoard {

    private short[][] grid;
    private List<Coordinate> fortresses;
    private int score;
    private KingMoveMode kingMode;

    public SmallTablutBoard (TablutBoard board, KingMoveMode mode) {
        this.grid = board.convertGridToInt();
        kingMode = mode;
    }

    public SmallTablutBoard (SmallTablutBoard s, KingMoveMode mode) {
        this.grid = s.getGrid();
        kingMode = mode;
    }

    public short[][] getGrid() {
        return grid;
    }

    public int getScore() {
        return score;
    }

    public void setKingMode(KingMoveMode mode) {
        this.kingMode = mode;
    }

    public List<Move> computeAllValidCells(Team team) {
        List<Move> list = new ArrayList<>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                short cell = grid[i][j];
                if (cell == 0) {
                    continue;
                }

                if (team == Team.MUSCOVITES) {
                    if (cell == 2) {
                        List<Coordinate> pawnLegalMoves = computeSoldierValidCells(i, j);
                        for (Coordinate c : pawnLegalMoves)
                            list.add(new Move(i, j, c.getX(), c.getY()));
                    }
                } else {
                    if (cell == 1) {
                        List<Coordinate> pawnLegalMoves = computeSoldierValidCells(i, j);
                        for (Coordinate c : pawnLegalMoves)
                            list.add(new Move(i, j, c.getX(), c.getY()));
                    } else if (cell == 3) {
                        List<Coordinate> pawnLegalMoves = computeKingValidCells(i, j);
                        for (Coordinate c : pawnLegalMoves)
                            list.add(new Move(i, j, c.getX(), c.getY()));
                    }
                }
            }
        }
        return list;
    }

    public List<Coordinate> computeSoldierValidCells(int i, int j) {
        List<Coordinate> lst = new ArrayList<>();

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int inc = 1;
            int[] position = {i + vect[0] * inc, j + vect[1] * inc};

            while (position[0] >= 0 && position[0] < 9
                    && position[1] >= 0 && position[1] < 9
                    && grid[position[0]][position[1]] == 0
                    // TODO : add fortresses check
                    ) {

                lst.add(new Coordinate(position[0], position[1]));
                inc++;
                position[0] = i + vect[0] * inc;
                position[1] = j + vect[1] * inc;
            }
        }

        return lst;
    }

    public List<Coordinate> computeKingValidCells(int i, int j) {
        List<Coordinate> lst = new ArrayList<>();

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int inc = 1;
            int[] position = {i + vect[0] * inc, j + vect[1] * inc};
            if (kingMode == KingMoveMode.LIMITLESS) {
                while (position[0] >= 0 && position[0] < 9
                        && position[1] >= 0 && position[1] < 9
                        && grid[position[0]][position[1]] == 0) {

                    lst.add(new Coordinate(position[0], position[1]));
                    inc++;
                    position[0] = i + vect[0] * inc;
                    position[1] = j + vect[1] * inc;
                }
            } else {
                if (position[0] >= 0 && position[0] < 9 && 
                    position[1] >= 0 && position[1] < 9 && 
                    grid[position[0]][position[1]] == 0) {
                    lst.add(new Coordinate(position[0], position[1]));
                }
            }

        }

        return lst;
    }

    public void applyMove(Move move) {
        grid[move.destX][move.destY] = grid[move.sourceX][move.sourceY];
        grid[move.sourceX][move.sourceY] = 0;
    }

    public void print() {
        System.out.println("----------------------");
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }


}