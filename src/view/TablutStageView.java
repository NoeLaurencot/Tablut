package view;

import boardifier.model.GameStageModel;
import boardifier.view.ClassicBoardLook;
import boardifier.view.GameStageView;
import boardifier.view.TextLook;
import javafx.scene.paint.Color;
import model.*;

/**
 * HoleStageView has to create all the looks for all game elements created by the HoleStageFactory.
 * The desired UI is the following:
 * player            ╔═╗    ┏━━━┓
 *    A   B   C      ║1║    ┃ 1 ┃
 *  ╔═══╦═══╦═══╗    ╠═╣    ┣━━━┫
 * 1║   ║   ║   ║    ║2║    ┃ 2 ┃
 *  ╠═══╬═══╬═══╣    ╠═╣    ┣━━━┫
 * 2║   ║   ║   ║    ║3║    ┃ 3 ┃
 *  ╠═══╬═══╬═══╣    ╠═╣    ┣━━━┫
 * 3║   ║   ║   ║    ║4║    ┃ 4 ┃
 *  ╚═══╩═══╩═══╝    ╚═╝    ┗━━━┛
 * <p>
 * The UI constraints are :
 * - the main board has double-segments border, coordinates, and cells of size 2x4
 * - the black pot has double-segments border, will cells that resize to match what is within (or not)
 * - the red pot has simple-segment border, and cells have a fixed size of 2x4
 * <p>
 * main board can be instanciated directly as a ClassicBoardLook.
 * black pot could be instanciated directly as a TableLook, but in this demo a BlackPotLook subclass is created (in case of we want to modifiy the look in some way)
 * for red pot, a subclass RedPotLook of GridLook is used, in order to override the method that render the borders.
 */

public class TablutStageView extends GameStageView {
    int cellSize;

    public TablutStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
        cellSize = 70;
    }

    @Override
    public void createLooks() {
        TablutStageModel model = (TablutStageModel) gameStageModel;
        TablutBoard board = model.getBoard();

        TextLook text = new TextLook(3, "0x000000", model.getPlayerName());
        addLook(text);

        ClassicBoardLook boardLook = new TablutBoardLook(this.cellSize, board, 1, Color.MAROON, Color.BEIGE, 1, Color.TRANSPARENT, 1, Color.BLACK, true);
        addLook(boardLook);

        int r = board.getNbRows();
        int c = board.getNbCols();

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (i == r / 2 && j == c / 2) { // king position
                    King king = (King) board.getElement(i, j, 1);
                    KingLook look = new KingLook((int) (this.cellSize * 0.3), king);
                    addLook(look);

                    Fortress fortress = (Fortress) board.getElement(i, j);
                    FortressLook fLook = new FortressLook((int) (this.cellSize * 0.25), fortress);
                    addLook(fLook);

                } else if (board.getElement(i, j) instanceof Soldier) {
                    Soldier soldier = (Soldier) board.getElement(i, j);
                    SoldierLook look = new SoldierLook((int) (this.cellSize * 0.3), soldier);
                    addLook(look);
                } else if (board.getElement(i, j) instanceof Fortress) {
                    Fortress fortress = (Fortress) board.getElement(i, j);
                    FortressLook look = new FortressLook((int) (this.cellSize * 0.25), fortress);
                    addLook(look);
                }
            }
        }
    }
}
