package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import model.Fortress;

public class FortressLook extends ElementLook {
    private ImageView imageView;

    public FortressLook(int size, GameElement element) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(element);

        Image image = new Image(getClass().getResource("/fortress.png").toExternalForm());

        imageView = new ImageView(image);

        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        imageView.setX(-(size / 2.0));
        imageView.setY(-(size / 2.0));

        imageView.setOpacity(0.3);

        addNode(imageView);
    }

    protected void render() {

    }

}
