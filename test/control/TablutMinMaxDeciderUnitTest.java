package control;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Coordinate;
import model.Fortress;
import model.King;
import model.KingMoveMode;
import model.Move;
import model.SmallTablutBoard;
import model.Soldier;
import model.TablutBoard;
import model.TablutStageModel;
import model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TablutMinMaxDeciderUnitTest {

    Model model;
    Controller controller;
    TablutStageModel stage;
    TablutBoard board;
    TablutMinMaxDecider decider;

    @BeforeEach
    public void init() {
        model = new Model();
        stage = new TablutStageModel("tablut", model);
        board = new TablutBoard(0, 0, stage);
        stage.setBoard(board);
        stage.setFortresses(new Fortress[0]);
        model.setGameStage(stage);

        controller = Mockito.mock(Controller.class);
        decider = new TablutMinMaxDecider(model, controller);
    }

    @Test
    public void testGetGameParameters_UsesBoardWinCoords() {
        ArrayList<Coordinate> customWinCoords = new ArrayList<>();
        customWinCoords.add(new Coordinate(4, 4));
        board.setWinCoords(customWinCoords);

        decider.getGameParameters();

        SmallTablutBoard smallBoard = new SmallTablutBoard(board, KingMoveMode.LIMITLESS);
        smallBoard.getGrid()[4][4] = 3;

        assertTrue(decider.winSwedish(smallBoard));
    }

    @Test
    public void testDecide_ReturnsActionListAndBestMove() {
        setupSingleMuscoviteMove();

        ActionList actions = decider.decide();

        assertNotNull(actions);
        assertNotNull(decider.bestMove);
        assertFalse(decider.bestMove.sourceX == decider.bestMove.destX &&
                decider.bestMove.sourceY == decider.bestMove.destY);
    }

    @Test
    public void testMinmax_TerminalWinMuscovitesReturnsScore() {
        setupSingleMuscoviteMove();
        decider.decide();

        SmallTablutBoard smallBoard = new SmallTablutBoard(board, KingMoveMode.LIMITLESS);
        clearGrid(smallBoard.getGrid());

        int score = decider.minmax(smallBoard, 2, -20000, 20000, true);

        assertEquals(10000, score);
    }

    @Test
    public void testSmallEvaluate_SwedishWinReturnsNegative() {
        SmallTablutBoard smallBoard = new SmallTablutBoard(board, KingMoveMode.LIMITLESS);
        clearGrid(smallBoard.getGrid());
        smallBoard.getGrid()[0][0] = 3;

        int score = decider.smallEvaluate(smallBoard);

        assertEquals(-10000, score);
    }

    @Test
    public void testEvaluate_ScoringWithPawnsAndKing() {
        SmallTablutBoard smallBoard = new SmallTablutBoard(board, KingMoveMode.LIMITLESS);
        clearGrid(smallBoard.getGrid());
        smallBoard.getGrid()[4][4] = 3;
        smallBoard.getGrid()[4][5] = 2;
        smallBoard.getGrid()[6][6] = 2;
        smallBoard.getGrid()[4][3] = 1;

        int score = decider.evaluate(smallBoard);

        assertNotEquals(0, score);
    }

    @Test
    public void testWinSwedish_WhenKingOnWinCoord() {
        SmallTablutBoard smallBoard = new SmallTablutBoard(board, KingMoveMode.LIMITLESS);
        clearGrid(smallBoard.getGrid());
        smallBoard.getGrid()[0][0] = 3;

        assertTrue(decider.winSwedish(smallBoard));
    }

    @Test
    public void testWinMuscovites_WhenNoKing() {
        SmallTablutBoard smallBoard = new SmallTablutBoard(board, KingMoveMode.LIMITLESS);
        clearGrid(smallBoard.getGrid());

        assertTrue(decider.winMuscovites(smallBoard));
    }

    private void setupSingleMuscoviteMove() {
        model.setIdPlayer(1);

        King king = new King(stage);
        board.addElement(king, 4, 4);

        Soldier pawn = new Soldier(Team.MUSCOVITES, stage);
        Soldier blockerRight = new Soldier(Team.SWEDISH, stage);
        Soldier blockerDown = new Soldier(Team.SWEDISH, stage);
        board.addElement(pawn, 0, 0);
        board.addElement(blockerRight, 0, 1);
        board.addElement(blockerDown, 2, 0);
    }

    private void clearGrid(short[][] grid) {
        for (short[] row : grid) {
            Arrays.fill(row, (short) 0);
        }
    }
}
y