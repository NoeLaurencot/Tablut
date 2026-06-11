package view;

import boardifier.model.GameElement;
import boardifier.view.TextLook;

public class PlayerLook extends TextLook {

    Player player;

    public PlayerLook(int fontSize, String color, GameElement element, Player) {
        super(fontSize, color, element);
        this.player = player;
    }

    public void amogus() {
        if (player.getName()[0] == 's') {
            DropShadow boldHighlight = new DropShadow();
            boldHighlight.setRadius(2.0);
            boldHighlight.setSpread(0.8);
            boldHighlight.setColor(Color.YELLOW);
            text.setEffect();
        } else {
            DropShadow boldHighlight = new DropShadow();
            boldHighlight.setRadius(2.0);
            boldHighlight.setSpread(0.8);
            boldHighlight.setColor(Color.RED);
            text.setEffect();
        }

        render();
    }

}
