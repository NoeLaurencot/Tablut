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
    public TablutBoard(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 9x9 grid, named "Tablutboard", and in x,y in space
        super("tablutboard", x, y, 9 , 9, gameStageModel);
    }

    public void setValidCells(int sourceRow, int sourceCol) {
        resetReachableCells(false);
        List<Point> valid = computeValidCells(sourceRow, sourceCol);
        if (valid != null) {
            for(Point p : valid) {
                reachableCells[p.y][p.x] = true;
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
    public List<Point> computeValidCells(int sourceRow, int sourceCol) {
        List<Point> lst = new ArrayList<>();

        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        for (int[] vect : directions) {
            int inc = 1;
            int[] position = {sourceRow + vect[0]*inc, sourceCol + vect[1]*inc};
            //System.out.println(position[0] + " " + position[1]);


            while (position[0] >= 0 && position[0] < 9
                && position[1] >= 0 && position[1] < 9
                && isEmptyAt(position[0], position[1])) {
                lst.add(new Point(position[0], position[1]));
                inc++;
                position[0] = sourceRow + vect[0]*inc; 
                position[1] = sourceCol + vect[1]*inc;
            }
        }



         /*
        }
        TO FULFILL:
            - compute the list of cells that are valid to play taking the pawn value (i.e. number) into account.
            each Point in this list consists in couple x,y, where x is a column and y a row in the board.
         */
        return lst;
    }
}
