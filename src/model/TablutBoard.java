package model;

import boardifier.control.Logger;
import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

/**
 * Tablut main board represent the element where pawns are put when played
 * Thus, a simple ContainerElement with 3 rows and 3 column is needed.
 * Nevertheless, in order to "simplify" the work for the controller part,
 * this class also contains method to determine all the valid cells to put a
 * pawn with a given value.
 */
public class TablutBoard extends ContainerElement {
    private ArrayList<Coordinate> winCoords;


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
        List<Point> valid = computeValidCells(sourceRow, sourceCol);
        if (valid != null) {
            for (Point p : valid) {
                reachableCells[p.x][p.y] = true;
            }
        }

        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (reachableCells[i][j]) {
                    System.out.print(1);
                } else {
                    System.out.print(0);
                }
            }       
            System.out.println();

        }*/
        
    }

    public List<Point> computeValidCells(int sourceRow, int sourceCol) {
        List<Point> lst = new ArrayList<>();

        boolean isKing = getElement(sourceCol, sourceRow) instanceof King;
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int inc = 1;
            int[] position = {sourceRow + vect[0]*inc, sourceCol + vect[1]*inc};
            //System.out.println(position[0] + " " + position[1]);


            while (position[0] >= 0 && position[0] < 9
                && position[1] >= 0 && position[1] < 9
                && isEmptyAt(position[1], position[0])
                && (!(getGameStage().isFortressAt(position[0], position[1])) || isKing)) {
                
                lst.add(new Point(position[0], position[1]));
                inc++;
                position[0] = sourceRow + vect[0]*inc; 
                position[1] = sourceCol + vect[1]*inc;
            }
        }

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

    @Override
    public TablutStageModel getGameStage() {
        return (TablutStageModel)gameStageModel;
    }
}
