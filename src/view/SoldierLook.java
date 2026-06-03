package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Soldier;
import model.Team;

public class SoldierLook extends ElementLook {
    private Circle circle;

    public SoldierLook(int radius, GameElement element) {
        super(element);
        Soldier soldier = (Soldier) element; // unless you did somthing stupid in HoleStageView, it IS really a Pawn instance !
        circle = new Circle();
        circle.setRadius(radius);
        if (soldier.getTeam() == Team.MUSCOVITES) {
            circle.setFill(Color.RED);
        } else {
            circle.setFill(Color.YELLOW);
        }
        circle.setCenterX(0);
        circle.setCenterY(0);
        // add the circle to the look
        addShape(circle);
        // to fulfill ...
    }

    @Override
    public void onSelectionChange() {
        // to fulfill ...
    }

    public void onChange() {
    }

    protected void render() {

    }

}
