package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import model.King;
import model.Team;

public class KingLook extends ElementLook {

    public KingLook(GameElement element) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(element);
    }

    protected void render() {
        King king = (King)element;
    }

}
