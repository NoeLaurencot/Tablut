package model;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class SmallTablutBoard {

    private short[][] grid;
    private List<CoordinateByte> fortresses;
    private KingMoveMode kingMode;

    public SmallTablutBoard(TablutBoard board, KingMoveMode mode) {
        this.grid = board.convertGridToInt();
        kingMode = mode;

        fortresses = new ArrayList<>();
        for (Fortress fortress : board.getGameStage().getFortresses()) {
            fortresses.add(new CoordinateByte((byte)fortress.getX(), (byte)fortress.getY()));
        }
    }

    public SmallTablutBoard(SmallTablutBoard s, KingMoveMode mode) {
        this.grid = Arrays.stream(s.getGrid()).map(short[]::clone).toArray(short[][]::new);
        kingMode = mode;
        this.fortresses = s.getFortresses();
    }

    public short[][] getGrid() {
        return grid;
    }

    public List<CoordinateByte> getFortresses() {
        return fortresses;
    }

    public void setKingMode(KingMoveMode mode) {
        this.kingMode = mode;
    }

    public List<MoveByte> computeAllValidCells(Team team) {
        List<MoveByte> list = new ArrayList<>();

        for (byte i = 0; i < grid.length; i++) {
            for (byte j = 0; j < grid[i].length; j++) {
                short cell = grid[i][j];
                if (cell == 0) {
                    continue;
                }

                if (team == Team.MUSCOVITES) {
                    if (cell == 2) {
                        List<CoordinateByte> pawnLegalMoveBytes = computeSoldierValidCells(i, j);
                        for (CoordinateByte c : pawnLegalMoveBytes)
                            list.add(new MoveByte(i, j, c.getX(), c.getY()));
                    }
                } else {
                    if (cell == 1) {
                        List<CoordinateByte> pawnLegalMoveBytes = computeSoldierValidCells(i, j);
                        for (CoordinateByte c : pawnLegalMoveBytes)
                            list.add(new MoveByte(i, j, c.getX(), c.getY()));
                    } else if (cell == 3) {
                        List<CoordinateByte> pawnLegalMoveBytes = computeKingValidCells(i, j);
                        for (CoordinateByte c : pawnLegalMoveBytes)
                            list.add(new MoveByte(i, j, c.getX(), c.getY()));
                    }
                }
            }
        }
        return list;
    }

    public List<CoordinateByte> computeSoldierValidCells(int i, int j) {
        List<CoordinateByte> lst = new ArrayList<>();

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int inc = 1;
            int[] position = {i + vect[0] * inc, j + vect[1] * inc};

            while (position[0] >= 0 && position[0] < 9
                    && position[1] >= 0 && position[1] < 9
                    && grid[position[0]][position[1]] == 0
                    && !isFortressAt(position[0], position[1])) {

                lst.add(new CoordinateByte((byte)position[0], (byte)position[1]));
                inc++;
                position[0] = i + vect[0] * inc;
                position[1] = j + vect[1] * inc;
            }
        }

        return lst;
    }

    public List<CoordinateByte> computeKingValidCells(int i, int j) {
        List<CoordinateByte> lst = new ArrayList<>();

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int inc = 1;
            int[] position = {i + vect[0] * inc, j + vect[1] * inc};
            if (kingMode == KingMoveMode.LIMITLESS) {
                while (position[0] >= 0 && position[0] < 9
                        && position[1] >= 0 && position[1] < 9
                        && grid[position[0]][position[1]] == 0) {

                    lst.add(new CoordinateByte((byte)position[0], (byte)position[1]));
                    inc++;
                    position[0] = i + vect[0] * inc;
                    position[1] = j + vect[1] * inc;
                }
            } else {
                if (position[0] >= 0 && position[0] < 9 && 
                    position[1] >= 0 && position[1] < 9 && 
                    grid[position[0]][position[1]] == 0) {
                    lst.add(new CoordinateByte((byte)position[0], (byte)position[1]));
                }
            }

        }

        return lst;
    }

    public CoordinateByte getKingPosition() {
        for (byte i = 0; i < grid.length; i++) {
            for (byte j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 3) {
                    return new CoordinateByte(i, j);
                }
            }
        }
        return new CoordinateByte((byte)-1, (byte)-1);
    }

    public void applyMove(MoveByte moveByte) {
        if (grid[moveByte.destX][moveByte.destY] != 0) {
            System.out.println("chiasse");
            print();
            System.out.println(moveByte);
        }
        grid[moveByte.destX][moveByte.destY] = grid[moveByte.sourceX][moveByte.sourceY];
        grid[moveByte.sourceX][moveByte.sourceY] = 0;
        afterPlay(moveByte.destX, moveByte.destY);
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

            // like King::shouldDie()
            if (ennemyPawn == 3) {
                int[] neighbor = {position[0] + vect[1], position[1] + vect[0]};
                if (grid[neighbor[0]][neighbor[1]] != 2 && !isFortressAt(neighbor[0], neighbor[1])) continue;
                
                neighbor[0] -= 2*vect[1];
                neighbor[1] -= 2*vect[0];
                if (grid[neighbor[0]][neighbor[1]] != 2 && !isFortressAt(neighbor[0], neighbor[1])) continue;
            }

            // checks the further cell if it is a ally pawn (capture)
            position[0] += vect[0];
            position[1] += vect[1];

            if (position[0] < 0 || position[0] > 8) continue;
            if (position[1] < 0 || position[1] > 8) continue;

            if (grid[position[0]][position[1]] == 0 && !isFortressAt(position[0], position[1]))
                continue;

            short otherPawn = grid[position[0]][position[1]];

            if (otherPawn == pawn || (isFortressAt(position[0], position[1]) && grid[position[0]][position[1]] != 3)) {
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

    public boolean isFortressAt(int x, int y) {
        for (CoordinateByte fortress : fortresses) {
            if (fortress.getX() == x && fortress.getY() == y) {
                return true;
            }
        }
        return false;
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