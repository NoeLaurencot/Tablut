package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Pawn;

public class PawnLook extends ElementLook {

    public PawnLook(GameElement element) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(element);
    }

    @Override
    public void onSelectionChange() {
        if (this.element.isSelected()) {
            ImageView img = (ImageView) getGroup().getChildren().getFirst();

            DropShadow boldHighlight = new DropShadow();
            boldHighlight.setRadius(5.0);
            boldHighlight.setSpread(0.8);
            boldHighlight.setColor(Color.YELLOW);

            img.setEffect(boldHighlight);
        } else {
            ImageView img = (ImageView) getGroup().getChildren().getFirst();
            img.setEffect(null);
        }
    }

    protected void render() {
        Pawn pawn = (Pawn)element;
    }

}
