package model;

import boardifier.model.ContainerElement;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

import java.awt.*;
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

                Pawn pawn = (Pawn) cell.getFirst();
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
     * @param sourceRow
     * @param sourceCol
     * @return
     */
    public List<Coordinate> computeValidCells(int sourceRow, int sourceCol) {

        List<Coordinate> lst = new ArrayList<Coordinate>();
        if (isEmptyAt(sourceRow, sourceCol)) return lst;

        Pawn pawn = (Pawn) getElement(sourceRow, sourceCol);
        lst = pawn.getLegalMoves();

        return lst;
    }

    public Coordinate getKingPosition() {
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                if (getElement(i, j) instanceof King) {
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

        if (kingPos.getX() == -1 && kingPos.getY() == -1) {
            return true;
        }
        return false;
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

    public void storeGrid() {
        storedGrid = new List[nbRows][nbCols];

        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                storedGrid[i][j] = grid[i][j];
            }
        }
    }

    public void restoreGrid() {
        for (int i = 0; i < nbRows; i++) {
            for (int j = 0; j < nbCols; j++) {
                grid[i][j] = storedGrid[i][j];
            }
        }
    }
}
