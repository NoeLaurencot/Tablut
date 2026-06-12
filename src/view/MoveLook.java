package view;

import boardifier.model.GameElement;
import boardifier.view.TextLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.MoveElement;

public class MoveLook extends TextLook {
    public Rectangle background;

    public MoveLook(GameElement element, int col, String color) {
        super(18, color, element);

        if (col == 0) {
            text.setX(element.getX() + 30);
            background = new Rectangle(50, 25, Color.BLACK);
            background.setArcWidth(5);
            background.setArcHeight(5);
            background.setX(element.getX() + 10);
            background.setY(element.getY() - 8);
            background.setVisible(false);

            getGroup().getChildren().addFirst(background);
            getShapes().add(background);
        }
        text.setY(element.getY() + 10);
    }

    @Override
    public void render() {
        super.render(); // updates the text from the element
        if (background != null) {
            MoveElement me = (MoveElement) getElement();
            boolean hasNumber = me.getText() != null && !me.getText().isEmpty();
            background.setVisible(hasNumber);

            int offset = 0;
            if (!this.text.getText().isEmpty())
                offset = center(Integer.parseInt(this.text.getText()), 5);
            this.text.setX(element.getX() + 30 - offset);
        }
    }

    public static int center(double x, int constant) {
        return constant * (int) Math.floor(Math.log10(x));
    }
}
