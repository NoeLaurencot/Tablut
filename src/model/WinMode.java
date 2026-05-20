package model;

import java.util.ArrayList;

public enum WinMode {
    BORDERS,
    CORNERS;

    public ArrayList<Coordinate> winCoords(TablutBoard board) {
        ArrayList<Coordinate> winCoords = new ArrayList<>();
        if (this == BORDERS) {
            for (int i = 0; i < board.getNbCols(); i++) {
                for (int j = 0; j < board.getNbRows(); j++) {
                    if (j == 0 || i == 0 || j == board.getNbRows() - 1 || i == board.getNbCols() - 1) {
                        winCoords.add(new Coordinate(i, j));
                    }
                }
            }
        } else {
            winCoords.add(new Coordinate(0, 0));
            winCoords.add(new Coordinate(board.getNbRows() - 1, 0));
            winCoords.add(new Coordinate(0, board.getNbCols() - 1));
            winCoords.add(new Coordinate(board.getNbRows() - 1, board.getNbCols() - 1));
        }
        return winCoords;
    }
}
