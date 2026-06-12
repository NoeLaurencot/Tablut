package view;

import boardifier.model.GameElement;
import boardifier.model.Player;
import boardifier.view.TextLook;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Team;

public class PlayerNameLook extends TextLook {
    private ImageView imageView;

    Player player;

    public PlayerNameLook(int fontSize, String color, GameElement element, Player player) {
        super(fontSize, color, element);
        this.player = player;

        Image image;

        if (player.getName().contains("swedish")) {
            image = new Image(getClass().getResource("/images/shield.png").toExternalForm());
        } else {
            image = new Image(getClass().getResource("/images/sword.png").toExternalForm());
        }

        imageView = new ImageView(image);

        double size = 45;
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        imageView.setX(-size*1.2);
        imageView.setY(-(size / 2.0));

        imageView.setSmooth(true);

        addNode(imageView);

    }

    public void highlightCurName(boolean isCurrent) {
        if (isCurrent) {
            DropShadow highlight = new DropShadow();
            highlight.setRadius(10.0);
            highlight.setSpread(0.6);
            highlight.setColor(Color.ORANGE);
            imageView.setEffect(highlight);
        } else {
            imageView.setEffect(null);
        }
    }

}
