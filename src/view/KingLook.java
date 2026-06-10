package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import model.King;

public class KingLook extends PawnLook {
    private ImageView imageView;

    public KingLook(int size, GameElement element) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(element);

        Image image = new Image(
                getClass().getResource("/king.png").toExternalForm()
        );

        imageView = new ImageView(image);

        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        imageView.setX(-(size / 2.0));
        imageView.setY(-(size / 2.0));

        addNode(imageView);
    }

    protected void render() {
        King king = (King) element;
    }

}
