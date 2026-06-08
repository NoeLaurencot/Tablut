package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import model.King;

public class KingLook extends ElementLook {
    private ImageView imageView;

    public KingLook(int radius, GameElement element) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(element);
        King king = (King) element;

        Image image = new Image(
                getClass().getResource("/king.png").toExternalForm()
        );

        imageView = new ImageView(image);

        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        imageView.setY(-25);
        imageView.setX(-25);

        addNode(imageView);
    }

    protected void render() {
        King king = (King) element;
    }

}
