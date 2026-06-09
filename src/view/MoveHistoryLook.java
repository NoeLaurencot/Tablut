package view;

import boardifier.model.ContainerElement;
import boardifier.view.GridLook;
import model.MoveHistory;
import model.MoveElement;

import javafx.scene.paint.Color;

public class MoveHistoryLook extends GridLook {

    public MoveHistoryLook(ContainerElement grid) {
        // Pawn look is constituted of a single character, so shape size = 1x1
        super(30, 70, grid, 1, 2, Color.BLACK);
    }
}
