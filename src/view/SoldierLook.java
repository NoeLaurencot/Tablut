package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Soldier;
import model.Team;

public class SoldierLook extends PawnLook {
    private ImageView imageView;

    public SoldierLook(int size, GameElement element) {
        super(element);
        Soldier soldier = (Soldier) element; // unless you did somthing stupid in HoleStageView, it IS really a Pawn instance !

        Image image;

        if (soldier.getTeam() == Team.MUSCOVITES) {
            image = new Image(getClass().getResource("/sword.png").toExternalForm());
        } else {
            image = new Image(getClass().getResource("/shield.png").toExternalForm());
        }

        imageView = new ImageView(image);

        imageView.setFitWidth(size);
        imageView.setFitHeight(size);

        imageView.setX(-(size / 2.0));
        imageView.setY(-(size / 2.0));

        imageView.setSmooth(true);

        addNode(imageView);

        // add the circle to the look

        // to fulfill ...
    }

    public void onChange() {
    }

    protected void render() {

    }

}
