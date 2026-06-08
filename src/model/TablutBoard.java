package model;

import boardifier.model.ContainerElement;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Tablut main board represent the element where pawns are put when played
 * Thus, a simple ContainerElement with 3 rows and 3 column is needed.
 * Nevertheless, in order to "simplify" the work for the controller part,
 * this class also contains method to determine all the valid cells to put a
 * pawn with a given value.
 */
public class TablutBoard extends ContainerElement {
    //                             UP     RIGHT   DOWN     LEFT
    final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private ArrayList<Coordinate> winCoords;
    private List<GameElement>[][] storedGrid;


    public TablutBoard(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 9x9 grid, named "Tablutboard", and in x,y in space
        super("tablutboard", x, y, 9, 9, gameStageModel);
        this.winCoords = new ArrayList<>();
        // default win coordinates
        for (int i = 0; i < nbCols; i++) {
            for (int j = 0; j < nbRows; j++) {
                if (j == 0 || i == 0 || j == nbRows - 1 || i == nbCols - 1) {
                    winCoords.add(new Coordinate(i, j));
                }
            }
        }
    }

    public ArrayList<Coordinate> getWinCoords() {
        return winCoords;
    }

    public void setWinCoords(ArrayList<Coordinate> winCoords) {
        this.winCoords = winCoords;
    }

    public void setValidCells(int sourceRow, int sourceCol) {
        resetReachableCells(false);
        List<Coordinate> valid = computeValidCells(sourceRow, sourceCol);
        if (valid != null) {
            for (Coordinate c : valid) {
                reachableCells[c.getX()][c.getY()] = true;
            }
        }

        /*
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (reachableCells[i][j]) {
                    System.out.print(1);
                } else {
                    System.out.print(0);
                }
            }       
            System.out.println();

        }
        */

    }

    public List<Move> computeAllValidCells(Team team) {
        List<Move> list = new ArrayList<>();

        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                List<GameElement> cell = grid[i][j];
                if (cell.isEmpty()) {
                    continue;
                }

                Pawn pawn = (Pawn) cell.getLast();
                if (pawn.getTeam() == team) {
                    List<Coordinate> pawnLegalMoves = pawn.getLegalMoves();
                    for (Coordinate c : pawnLegalMoves)
                        list.add(new Move(i, j, c.getX(), c.getY()));
                }
            }
        }
        return list;
    }

    /**
     * Checks if the team is in a stalemate, if so, they lose
     *
     * @param team The team to check if in stalemate
     * @return If the team is in stalemate and thus, should lose
     */
    public boolean isStalemate(Team team) {
        return computeAllValidCells(team).isEmpty();
    }

    /**
     * Return a List Point Who
     *
     * @param sourceRow
     * @param sourceCol
     * @return
     */
    public List<Coordinate> computeValidCells(int sourceRow, int sourceCol) {

        List<Coordinate> lst = new ArrayList<Coordinate>();
        if (isEmptyAt(sourceRow, sourceCol)) return lst;

        Pawn pawn = (Pawn) getLastElement(sourceRow, sourceCol);
        lst = pawn.getLegalMoves();

        return lst;
    }

    public boolean isFortressAt(int i, int j) {
        return grid[i][j].getFirst() instanceof Fortress;
    }

    public boolean isPawnAt(int i, int j) {
        if (grid[i][j].size() == 0) {
            return false;
        }
        return grid[i][j].getLast() instanceof Soldier;
    }

    public Coordinate getKingPosition() {
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                if (getLastElement(i, j) instanceof King) {
                    return new Coordinate(i, j);
                }
            }
        }
        return new Coordinate(-1, -1);
    }

    public boolean areSwedishWinning() {
        Coordinate kingPos = getKingPosition();

        for (Coordinate coord : getWinCoords()) {
            if (coord.getX() == kingPos.getX() && coord.getY() == kingPos.getY()) {
                return true;
            }
        }
        return false;
    }

    public boolean areMuscovitesWinning() {
        Coordinate kingPos = getKingPosition();

        return kingPos.getX() == -1 && kingPos.getY() == -1;
    }

    public short[][] convertGridToInt() {
        short[][] map = new short[nbRows][];

        for (int i = 0; i < nbRows; i++) {
            map[i] = new short[nbCols];
            for (int j = 0; j < nbCols; j++) {
                Pawn pawn = (Pawn) getElement(i, j);

                if (pawn instanceof Soldier) {
                    if (pawn.getTeam() == Team.SWEDISH) {
                        map[i][j] = 1;
                    } else {
                        map[i][j] = 2;
                    }
                } else if (pawn instanceof King) {
                    map[i][j] = 3;
                } else {
                    map[i][j] = 0;
                }
            }
        }

        return map;
    }

    @Override
    public TablutStageModel getGameStage() {
        return (TablutStageModel) gameStageModel;
    }

    public void setGrid(short[][] map) {
        reset();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 1) {
                    addElement(new Soldier(i, j, Team.SWEDISH, getGameStage()), i, j);
                } else if (map[i][j] == 2) {
                    addElement(new Soldier(i, j, Team.MUSCOVITES, getGameStage()), i, j);
                } else if (map[i][j] == 3) {
                    addElement(new King(i, j, getGameStage()), i, j);
                }
            }
        }
    }

    public King findKing() {
        for (int i = 0; i < this.nbRows; i++) {
            for (int j = 0; j < this.nbCols; j++) {
                if (getElement(i, j) instanceof King)
                    return (King) getElement(i, j);
            }
        }
        return null;
    }

    public boolean outOfBounds(Coordinate coord) {
        return coord.getX() < 0 || coord.getX() > this.nbRows - 1 ||
                coord.getY() < 0 || coord.getY() > this.nbCols - 1;
    }

    public boolean canSeeCoordinate(Coordinate srcCoord, Coordinate destCoord) {
        return getFarthestVisibleCoords(srcCoord).contains(destCoord);
    }

    public boolean canSeeKing(Coordinate coord) {
        King king = findKing();
        Coordinate kingCoord = new Coordinate(king.getXInBoard(), king.getYInBoard());

        return canSeeCoordinate(coord, kingCoord);
    }

    public ArrayList<Coordinate> getAdjacentCoords(Coordinate coord) {
        ArrayList<Coordinate> adjacentPawnsCoords = new ArrayList<>();

        for (final int[] VECT : DIRECTIONS) {
            Coordinate position = new Coordinate(coord);

            position.increment(VECT[0], VECT[1]);

            if (!outOfBounds(position)) {
                adjacentPawnsCoords.add(position);
            }
        }
        return adjacentPawnsCoords;
    }

    public ArrayList<Coordinate> getAdjacentEnemyPawnsCoords(Coordinate coord, Team team) {
        ArrayList<Coordinate> adjacentCoords = getAdjacentCoords(coord);
        ArrayList<Coordinate> enemyCoords = new ArrayList<>();

        for (Coordinate adjCoord : adjacentCoords) {
            // Only add if not empty and team is different
            if (!this.isEmptyAt(adjCoord.getX(), adjCoord.getY())) {
                GameElement element = getElement(adjCoord.getX(), adjCoord.getY());
                if (((Pawn) element).getTeam() != team) {
                    enemyCoords.add(adjCoord);
                }
            }
        }
        return enemyCoords;
    }

    public Coordinate getFarthestVisibleCoord(Coordinate coord, int[] vect) {
        Coordinate position = new Coordinate(coord);

        do {
            position.increment(vect[0], vect[1]);
        } while (!outOfBounds(position) && isEmptyAt(position.getX(), position.getY()));

        if (outOfBounds(position)) {
            // goes back to prev position
            position.increment(-vect[0], -vect[1]);
            // doesn't return if original coords
            if (!position.equals(coord)) {
                return position;
            }
        } else {
            return position;
        }
        return null;
    }

    public ArrayList<Coordinate> getFarthestVisibleCoords(Coordinate coord) {
        ArrayList<Coordinate> farthestVisibleCells = new ArrayList<>();

        for (final int[] VECT : DIRECTIONS) {
            Coordinate visibleCoord = getFarthestVisibleCoord(coord, VECT);
            if (visibleCoord != null)
                farthestVisibleCells.add(visibleCoord);
        }
        return farthestVisibleCells;
    }

    public ArrayList<Coordinate> getFarthestVisibleEnemyCoords(Coordinate coord, Team currentTeam) {
        ArrayList<Coordinate> enemiesCoords = new ArrayList<>();

        for (Coordinate farCoord : getFarthestVisibleCoords(coord)) {
            // if there is enemy at coordinates
            if (!isEmptyAt(farCoord.getX(), farCoord.getY()) && ((Pawn) getElement(farCoord.getX(), farCoord.getY())).getTeam() != currentTeam) {
                enemiesCoords.add(farCoord);
            }
        }
        return enemiesCoords;
    }

    public boolean isAnyEnemyVisible(Coordinate coord, Team currentTeam) {
        return getFarthestVisibleEnemyCoords(coord, currentTeam).size() > 0;
    }

    public boolean wouldBeSurroundedIfEnemyAt(Coordinate pawnCoord, Coordinate enemyCoord, Pawn pawn) {

        Team team = pawn.getTeam();

        if (pawn instanceof King) {
            return this.countEnemyNeighbors(pawnCoord, team) == 3;
        } else {
            for (Coordinate coordAdj : this.getAdjacentEnemyPawnsCoords(pawnCoord, team)) {
                if (enemyCoord.getX() == coordAdj.getX() || enemyCoord.getY() == coordAdj.getY())
                    return true;
            }
        }

        return false;
    }

    public ArrayList<Coordinate> getEmptyNeighbors(Coordinate coord) {
        ArrayList<Coordinate> emptyCoords = new ArrayList<>();

        for (Coordinate emptyCoord : getAdjacentCoords(coord)) {
            if (this.isEmptyAt(emptyCoord.getX(), emptyCoord.getY())) {
                emptyCoords.add(emptyCoord);
            }
        }

        return emptyCoords;
    }

    public int countEnemyNeighbors(Coordinate coord, Team team) {
        ArrayList<Coordinate> coords = getAdjacentEnemyPawnsCoords(coord, team);

        if (coords != null)
            return coords.size();

        return 0;
    }

    public int captureCount(Coordinate coord, Team team) {
        int count = 0;

        for (Coordinate enemyCoord : getAdjacentEnemyPawnsCoords(coord, team)) {
            if (((Pawn) getElement(enemyCoord.getX(), enemyCoord.getY())).wouldBeSurroundedIfEnemyAt(coord)) {
                count++;
            }
        }
        return count;
    }

    public int countVisibleBorder(Coordinate coord) {
        int counter = 0;
        for (Coordinate farCoord : getFarthestVisibleCoords(coord)) {
            boolean isBorders = (farCoord.getX() == 0 || farCoord.getX() == this.nbRows - 1) || (farCoord.getY() == 0 || farCoord.getY() == this.nbCols - 1);
            if (this.isEmptyAt(farCoord.getX(), farCoord.getY()) && isBorders) {
                counter++;
            }
        }
        return counter;
    }

    public void debugPrint() {
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                if (!grid[i][j].isEmpty()) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
