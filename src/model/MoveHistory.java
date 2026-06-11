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
public class MoveHistory extends ContainerElement {

    private int insertCol = 0;
    private int insertRow = 0;

    public MoveHistory(int x, int y, GameStageModel gameStageModel) {
        super("movehistory", x, y, 20, 2, gameStageModel);
    }

    public void addMove(Move move) {
        addElement(new MoveElement(move, getGameStage()), insertRow, insertCol);
        if (insertCol == 1) {
            insertCol = 0;
            insertRow++;
        } else {
            insertCol++;
        }
    }

    public void addEmptyMove(MoveElement moveElement) {
        addElement(moveElement, insertRow, insertCol);
        if (insertCol == 1) {
            insertCol = 0;
            insertRow++;
        } else {
            insertCol++;
        }
    }

    public void addMove(MoveElement moveElement) {
        MoveElement empty = (MoveElement) getElement(insertRow, insertCol);
        empty.setText(moveElement.getMove().toStringHistory());
        if (insertCol == 1) {
            insertCol = 0;
            insertRow++;
        } else {
            insertCol++;
        }

        if (insertRow >= getNbRows()) {
            resetInsertMove();
        }
    }

    public void resetInsertMove() {
        insertRow = 0;
        insertCol = 0;
    }
}
