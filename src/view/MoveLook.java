package view;

import boardifier.model.GameElement;
import boardifier.view.TextLook;
import model.MoveElement;

public class MoveLook extends TextLook {

    public MoveLook(GameElement element) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(20, "0x000", element);
    }

}
