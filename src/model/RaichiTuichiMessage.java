package model;

import boardifier.model.GameStageModel;
import boardifier.model.TextElement;

/**
 * Tablut main board represent the element where pawns are put when played
 * Thus, a simple ContainerElement with 3 rows and 3 column is needed.
 * Nevertheless, in order to "simplify" the work for the controller part,
 * this class also contains method to determine all the valid cells to put a
 * pawn with a given value.
 */
public class RaichiTuichiMessage extends TextElement {

    private int insertCol = 0;
    private int insertRow = 0;

    public RaichiTuichiMessage(int x, int y, GameStageModel gameStageModel) {
        super("", gameStageModel);
        setLocation(x, y);
    }
}
