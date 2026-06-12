package model;

import boardifier.model.ContainerElement;
import boardifier.model.GameStageModel;

import java.util.ArrayList;

/**
 * Tablut main board represent the element where pawns are put when played
 * Thus, a simple ContainerElement with 3 rows and 3 column is needed.
 * Nevertheless, in order to "simplify" the work for the controller part,
 * this class also contains method to determine all the valid cells to put a
 * pawn with a given value.
 */
public class MoveHistory extends ContainerElement {
    private static final int NB_ROWS = 18;
    private static final int NB_COLS = 3;

    private int totalRows;

    private int insertCol = 0;
    private int insertRow = 0;
    public MoveHistory(int x, int y, GameStageModel gameStageModel) {
        super("movehistory", x, y, NB_ROWS, NB_COLS, gameStageModel);
        this.totalRows = 1;
    }

    public void addMove(Move move) {
        if (insertCol == 0) {
            addElement(new MoveElement(String.valueOf(totalRows), getGameStage()), insertRow, insertCol);
            insertCol++;
        }
        addElement(new MoveElement(move, getGameStage()), insertRow, insertCol);
        if (insertCol == 2) {
            insertCol = 0;
            totalRows++;
            insertRow++;
        } else {
            insertCol++;
        }
    }

    public void addEmptyMove(MoveElement moveElement) {
        addElement(moveElement, insertRow, insertCol);
        if (insertCol == 2) {
            insertCol = 0;
            insertRow++;
        } else {
            insertCol++;
        }
    }

    public void reset() {
        ((MoveElement) getElement(0, 0)).setText("");
        insertCol = 0;
        insertRow = 0;
    }

    public void shiftHistoryUp() {
        for (int i = 0; i < this.nbRows - 1; i++) {
            for (int j = 0; j < this.nbCols; j++) {
                ArrayList<MoveElement> curMoveElementRow = new ArrayList<>();
                ArrayList<MoveElement> nextMoveElementRow = new ArrayList<>();

                curMoveElementRow.add((MoveElement) getElement(i, j));
                nextMoveElementRow.add((MoveElement) getElement(i + 1, j));

                setRow(curMoveElementRow, nextMoveElementRow);
                clearRow(nextMoveElementRow);
            }
        }

    }

    public void setRow(ArrayList<MoveElement> row, ArrayList<MoveElement> nextRow) {
        for (int i = 0; i < row.size(); i++) {
            row.get(i).setText(nextRow.get(i).getText());
        }
    }

    public void clearRow(ArrayList<MoveElement> row) {
        for (MoveElement element : row) {
            element.clear();
        }
    }

    public void addMove(MoveElement moveElement) {
        if (insertRow >= this.nbRows) {
            shiftHistoryUp();
            insertRow--;
        }

        MoveElement gridCell = (MoveElement) getElement(insertRow, insertCol);
        if (insertCol == 0) {
            gridCell.setText(String.valueOf(totalRows));
            insertCol++;
        }

        gridCell = (MoveElement) getElement(insertRow, insertCol);
        gridCell.setText(moveElement.getMove().toStringHistory());

        if (insertCol == 2) {
            insertCol = 0;
            totalRows++;
            insertRow++;
        } else {
            insertCol++;
        }
    }

    public void resetInsertMove() {
        insertRow = 0;
        insertCol = 0;
    }
}
