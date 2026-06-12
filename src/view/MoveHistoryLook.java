package view;

import boardifier.model.ContainerElement;
import boardifier.view.GridLook;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import model.MoveHistory;
import model.MoveElement;

import javafx.scene.paint.Color;

public class MoveHistoryLook extends GridLook {

    public MoveHistoryLook(ContainerElement grid) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(30, 70, grid, 2, 1, Color.TRANSPARENT);
    }

    @Override
    protected void render() {
        // create borders if needed
        if (borderWidth >= 1) {
            borders = new Rectangle[nbRows][nbCols];
            for(int i=0;i<nbRows;i++) {
                for (int j = 0; j < nbCols; j++) {
                    borders[i][j] = new Rectangle(colWidth, rowHeight, Color.TRANSPARENT);
                    borders[i][j].setSmooth(true);
                    borders[i][j].setStroke(borderColor);
                    borders[i][j].setStrokeWidth(borderWidth);
                    borders[i][j].setStrokeMiterLimit(10);
                    borders[i][j].setStrokeType(StrokeType.CENTERED);
                    borders[i][j].setX(innersLeft + j * colWidth);
                    borders[i][j].setY(innersTop + i * rowHeight);

                    addShape(borders[i][j]);
                }
            }
        }
        else {
            borders = null;
        }
    }
}
