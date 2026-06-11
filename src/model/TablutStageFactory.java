package model;

import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

import java.util.ArrayList;
import java.util.List;

/**
 * TablutStageFactory must create the game elements that are defined in TablutStageModel
 * WARNING: it just creates the game element and NOT their look, which is done in TablutStageView.
 * <p>
 * If there must be a precise position in the display for the look of a game element, then this element must be created
 * with that position in the virtual space and MUST NOT be placed in a container element. Indeed, for such
 * elements, the position in their virtual space will match the position on the display. For example, in the following,
 * the black pot is placed in 18,0. When displayed on screen, the top-left character of the black pot will be effectively
 * placed at column 18 and row 0.
 * <p>
 * Otherwise, game elements must be put in a container and it will be the look of the container that will manage
 * the position of element looks on the display. For example, pawns are put in a ContainerElement. Thus, their virtual space is
 * in fact the virtual space of the container and their location in that space in managed by boardifier, depending of the
 * look of the container.
 *
 */
public class TablutStageFactory extends StageElementsFactory {
    private TablutStageModel stageModel;

    public TablutStageFactory(GameStageModel gameStageModel) {
        super(gameStageModel);
        stageModel = (TablutStageModel) gameStageModel;
    }

    @Override
    public void setup() {
        TextElement text = new TextElement(stageModel.getCurrentPlayerName(), stageModel);
        text.setLocation(0, 0);
        text.setVisible(false);
        stageModel.setPlayerName(text);

        RaichiTuichiMessage message = new RaichiTuichiMessage(300, 610, stageModel);
        stageModel.setMessage(message);

        TablutBoard board = new TablutBoard(0, 1, stageModel);
        stageModel.setBoard(board);

        setupFortresses();
        setupPawns();
        setupMoveHistory();

    }

    private void setupPawns() {
        Pawn[] pawns = new Pawn[25];

        // Swedish
        pawns[0] = new Soldier(4, 2, Team.SWEDISH, stageModel);
        pawns[1] = new Soldier(4, 3, Team.SWEDISH, stageModel);
        pawns[2] = new Soldier(4, 5, Team.SWEDISH, stageModel);
        pawns[3] = new Soldier(4, 6, Team.SWEDISH, stageModel);
        pawns[4] = new Soldier(2, 4, Team.SWEDISH, stageModel);
        pawns[5] = new Soldier(3, 4, Team.SWEDISH, stageModel);
        pawns[6] = new Soldier(5, 4, Team.SWEDISH, stageModel);
        pawns[7] = new Soldier(6, 4, Team.SWEDISH, stageModel);


        // Moscovites
        pawns[8] = new Soldier(3, 0, Team.MUSCOVITES, stageModel);
        pawns[9] = new Soldier(4, 0, Team.MUSCOVITES, stageModel);
        pawns[10] = new Soldier(5, 0, Team.MUSCOVITES, stageModel);
        pawns[11] = new Soldier(4, 1, Team.MUSCOVITES, stageModel);

        pawns[12] = new Soldier(0, 3, Team.MUSCOVITES, stageModel);
        pawns[13] = new Soldier(0, 4, Team.MUSCOVITES, stageModel);
        pawns[14] = new Soldier(0, 5, Team.MUSCOVITES, stageModel);
        pawns[15] = new Soldier(1, 4, Team.MUSCOVITES, stageModel);

        pawns[16] = new Soldier(8, 3, Team.MUSCOVITES, stageModel);
        pawns[17] = new Soldier(8, 4, Team.MUSCOVITES, stageModel);
        pawns[18] = new Soldier(8, 5, Team.MUSCOVITES, stageModel);
        pawns[19] = new Soldier(7, 4, Team.MUSCOVITES, stageModel);

        pawns[20] = new Soldier(3, 8, Team.MUSCOVITES, stageModel);
        pawns[21] = new Soldier(4, 8, Team.MUSCOVITES, stageModel);
        pawns[22] = new Soldier(5, 8, Team.MUSCOVITES, stageModel);
        pawns[23] = new Soldier(4, 7, Team.MUSCOVITES, stageModel);

        // King

        pawns[24] = new King(4, 4, stageModel);

        stageModel.setPawns(pawns);
    }

    private void setupFortresses() {
        ArrayList<Fortress> fortressList = new ArrayList<>();

        int x = stageModel.getBoard().getNbCols();
        int y = stageModel.getBoard().getNbRows();

        if (TablutOptionModel.getWinMode() == WinMode.CORNERS) {
            fortressList.add(new Fortress(0, 0, stageModel));
            fortressList.add(new Fortress(y - 1, 0, stageModel));
            fortressList.add(new Fortress(0, x - 1, stageModel));
            fortressList.add(new Fortress(y - 1, x - 1, stageModel));
        }
        fortressList.add(new Fortress(y / 2.0, x / 2.0, stageModel));

        stageModel.setFortresses(fortressList);
    }

    private void setupMoveHistory() {
        MoveHistory moveHistory = new MoveHistory(600, 0, stageModel);

        int r = moveHistory.getNbRows();
        int c = moveHistory.getNbCols();

        List<MoveElement> emptyMoves = new ArrayList<>();

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                MoveElement moveElement = new MoveElement(new Move(), stageModel);
                emptyMoves.add(moveElement);
            }
        }

        stageModel.setMoveHistory(moveHistory);
        stageModel.setEmptyMovesHistory(emptyMoves);
    }
}
