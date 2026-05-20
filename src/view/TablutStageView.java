package view;

import boardifier.control.Logger;
import boardifier.model.GameStageModel;
import boardifier.view.ClassicBoardLook;
import boardifier.view.ContainerLook;
import boardifier.view.GameStageView;

import boardifier.view.TextLook;
import model.TablutStageModel;
import model.TablutBoard;
import model.Pawn;
import model.Soldier;
import model.King;

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
    public TablutStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    @Override
    public void createLooks() {
        TablutStageModel model = (TablutStageModel) gameStageModel;
        TablutBoard board = model.getBoard();

        TextLook text = new TextLook(model.getPlayerName());
        addLook(text);

        ClassicBoardLook boardLook = new ClassicBoardLook(2, 4, board, 1, 1, true);
        addLook(boardLook);

        int r = board.getNbRows();
        int c = board.getNbCols();

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (i == r / 2 && j == c / 2) { // king position
                    King king = (King) board.getElement(i, j);
                    KingLook look = new KingLook(king);
                    addLook(look);
                } else if (!board.isEmptyAt(i, j)) {
                    Soldier soldier = (Soldier) board.getElement(i, j);
                    SoldierLook look = new SoldierLook(soldier);
                    addLook(look);
                }
            }
        }
    }

    public void showOptionsMenu() {
        System.out.print("""
                1 - King move mode
                \ta - Without distance limitations
                \tb - Single square distance limitation
                2 - Win zone
                \ta - Borders
                \tb - Corners
               
                Choose an option (ex: 1a) >\s""");
    }
}
