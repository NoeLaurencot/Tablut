package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.King;
import model.Soldier;

public class KingLook extends ElementLook {
    private Circle circle;
    private Circle innerCircle;

    public KingLook(int radius, GameElement element) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(element);
        King king = (King) element;
        circle = new Circle();
        innerCircle = new Circle();
        circle.setRadius(radius);
        innerCircle.setRadius(radius * 0.5);
        circle.setFill(Color.YELLOW);
        innerCircle.setFill(Color.ORANGE);
        circle.setCenterX(0);
        circle.setCenterY(0);
        innerCircle.setCenterX(0);
        innerCircle.setCenterY(0);
        // add the circle to the look
        addShape(circle);
        addShape(innerCircle);
        // to fulfill ...
    }

    protected void render() {
        King king = (King) element;
    }

}
