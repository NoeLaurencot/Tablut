package model;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class SmallTablutBoard {

    private short[][] grid;
    private List<Coordinate> fortresses;
    private int score;
    private KingMoveMode kingMode;

    public SmallTablutBoard(TablutBoard board, KingMoveMode mode) {
        this.grid = board.convertGridToInt();
        kingMode = mode;
    }

    public SmallTablutBoard(SmallTablutBoard s, KingMoveMode mode) {
        this.grid = Arrays.stream(s.getGrid()).map(short[]::clone).toArray(short[][]::new);
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
        if (grid[move.destX][move.destY] != 0) {
            System.out.println("chiasse");
            print();
            System.out.println(move);
        }
        grid[move.destX][move.destY] = grid[move.sourceX][move.sourceY];
        grid[move.sourceX][move.sourceY] = 0;
        afterPlay(move.destX, move.destY);
    }

    private void afterPlay(int destRow, int destCol) {
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

        short pawn = grid[destRow][destCol];
        for (int[] vect : directions) {

            // first check if the next cell is an ennemy pawn
            int[] position = {destRow + vect[0], destCol + vect[1]};

            if (position[0] < 0 || position[0] > 8) continue;
            if (position[1] < 0 || position[1] > 8) continue;

            if (grid[position[0]][position[1]] == 0)
                continue;

            short ennemyPawn = grid[position[0]][position[1]];
            if (Math.abs(ennemyPawn - pawn) != 1)
                continue;

            // checks the further cell if it is a ally pawn (capture)
            position[0] += vect[0];
            position[1] += vect[1];

            if (position[0] < 0 || position[0] > 8) continue;
            if (position[1] < 0 || position[1] > 8) continue;

            if (grid[position[0]][position[1]] == 0 && true )
            // TODO add fortress check
                continue;

            short otherPawn = grid[position[0]][position[1]];

            if (otherPawn == pawn) {
                position[0] -= vect[0];
                position[1] -= vect[1];
                grid[position[0]][position[1]] = 0;
            }
        }
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

    @Override
    public String toString() {
        String st = "";
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                st += grid[i][j];
            }
        }
        return st;
    }


}