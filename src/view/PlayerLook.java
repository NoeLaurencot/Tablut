package view;

import boardifier.model.GameElement;
import boardifier.model.Player;
import boardifier.view.TextLook;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class PlayerLook extends TextLook {

    Player player;

    public PlayerLook(int fontSize, String color, GameElement element, Player player) {
        super(fontSize, color, element);
        this.player = player;
    }

    public void amogus() {
        if (player.getName().contains("swedish")) {
            DropShadow boldHighlight = new DropShadow();
            boldHighlight.setRadius(2.0);
            boldHighlight.setSpread(0.8);
            boldHighlight.setColor(Color.YELLOW);
        } else {
            DropShadow boldHighlight = new DropShadow();
            boldHighlight.setRadius(2.0);
            boldHighlight.setSpread(0.8);
            boldHighlight.setColor(Color.RED);
        }

        render();
    }

}
