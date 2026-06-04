package view;

import boardifier.model.ContainerElement;
import boardifier.view.ClassicBoardLook;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import model.King;
import model.Soldier;

public class TablutBoardLook extends ClassicBoardLook {
    
    private Circle[][] selectedCellCircles;

    public TablutBoardLook(int cellSize, ContainerElement element, int depth, Color evenColor, Color oddColor, int borderWidth, Color borderColor, int frameWidth, Color frameColor, boolean showCoords) {
        super(cellSize,element, depth, evenColor, oddColor, borderWidth, borderColor, frameWidth, frameColor, showCoords);
    }

    @Override
    protected void render() {
        super.render();
        
        selectedCellCircles = new Circle[nbRows][nbCols];
        for (int i=0;i< nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                Color c = Color.GREY;

                selectedCellCircles[i][j] = new Circle(j*colWidth+gapXToCells+colWidth/2, i*rowHeight+gapYToCells+rowHeight/2, 15);
                selectedCellCircles[i][j].setSmooth(false);
                selectedCellCircles[i][j].setStroke(c);
                selectedCellCircles[i][j].setFill(c);
                selectedCellCircles[i][j].setStrokeWidth(0);
                addShape(selectedCellCircles[i][j]);
            }
        }
    }

    @Override
    public void onFaceChange() {
        ContainerElement board = (ContainerElement)element;
        boolean[][] reach = board.getReachableCells();
        for(int i=0;i<nbRows;i++) {
            for(int j=0;j<nbCols;j++) {
                if (reach[i][j]) {
                    selectedCellCircles[i][j].setVisible(true);
                } else {
                    selectedCellCircles[i][j].setVisible(false);
                }
            }
        }
    }
}
