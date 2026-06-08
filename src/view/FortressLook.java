package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import model.Fortress;

public class FortressLook extends ElementLook {
    public FortressLook(int size, GameElement element) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(element);
        Fortress king = (Fortress) element;

        Line line1 = new Line();
        line1.setStrokeWidth(10);
        line1.setFill(Color.BLACK);
        line1.setStartX(-size);
        line1.setStartY(-size);
        line1.setEndX(size);
        line1.setEndY(size);
        Line line2 = new Line();
        line2.setStrokeWidth(10);
        line2.setFill(Color.BLACK);
        line2.setStartX(size);
        line2.setStartY(-size);
        line2.setEndX(-size);
        line2.setEndY(size);
        // add the circle to the look
        addShape(line1);
        addShape(line2);
    }

    protected void render() {

    }

}
