package control;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Coordinate;
import model.Fortress;
import model.King;
import model.Move;
import model.Soldier;
import model.TablutBoard;
import model.TablutStageModel;
import model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TablutSimpleDeciderUnitTest {

    Model model;
    Controller controller;
    TablutStageModel stage;
    TablutBoard board;
    TablutSimpleDecider decider;
    King king;
    Soldier movingPawn;

    @BeforeEach
    public void setup() {
        model = new Model();
        stage = new TablutStageModel("tablut", model);
        board = new TablutBoard(0, 0, stage);
        stage.setBoard(board);
        stage.setFortresses(new Fortress[0]);
        model.setGameStage(stage);
        model.setIdPlayer(0);

        controller = Mockito.mock(Controller.class);
        decider = new TablutSimpleDecider(model, controller);

        king = new King(stage);
        board.addElement(king, 4, 4);

        movingPawn = new Soldier(Team.SWEDISH, stage);
        board.addElement(movingPawn, 0, 0);

        decider.decide();
    }

    @Test
    public void testApplyGameParameters_UsesBoardWinCoords() {
        ArrayList<Coordinate> closeWinCoords = new ArrayList<>();
        closeWinCoords.add(new Coordinate(4, 5));
        board.setWinCoords(closeWinCoords);
        decider.applyGameParameters();

        Move move = new Move(4, 4, 4, 4);
        int scoreNear = decider.evaluateSwedish(board, move, king);

        ArrayList<Coordinate> farWinCoords = new ArrayList<>();
        farWinCoords.add(new Coordinate(0, 0));
        board.setWinCoords(farWinCoords);
        decider.applyGameParameters();

        int scoreFar = decider.evaluateSwedish(board, move, king);

        assertTrue(scoreNear > scoreFar);
    }

    @Test
    public void testDecide_ReturnsActionList() {
        ActionList result = decider.decide();

        assertNotNull(result);
        assertNotNull(decider.bestMove);
    }

    @Test
    public void testSimpleStrategy_SingleMoveSelected() {
        List<Move> moves = new ArrayList<>();
        Move move = new Move(0, 0, 0, 1);
        moves.add(move);

        ActionList result = decider.simpleStrategy(moves);

        assertNotNull(result);
        assertEquals(decider.bestMove, move);
    }

    @Test
    public void testEvaluateSwedish_WinningMoveReturnsMax() {
        Move move = new Move(4, 4, 4, 0);

        int result = decider.evaluateSwedish(board, move, king);

        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testEvaluateMuscovites_CapturesKingReturnsMax() {
        Soldier enemy1 = new Soldier(Team.MUSCOVITES, stage);
        Soldier enemy2 = new Soldier(Team.MUSCOVITES, stage);
        Soldier enemy3 = new Soldier(Team.MUSCOVITES, stage);
        board.addElement(enemy1, 4, 5);
        board.addElement(enemy2, 5, 4);
        board.addElement(enemy3, 3, 4);

        Soldier attacker = new Soldier(Team.MUSCOVITES, stage);
        Move move = new Move(0, 0, 4, 3);

        int result = decider.evaluateMuscovites(board, move, attacker);

        assertEquals(Integer.MAX_VALUE, result);
    }

    @Test
    public void testEvalCellDangerosity_WhenCapturableReturnsPenalty() {
        Soldier enemyAdjacent = new Soldier(Team.MUSCOVITES, stage);
        Soldier enemyInLine = new Soldier(Team.MUSCOVITES, stage);
        board.addElement(enemyAdjacent, 0, 2);
        board.addElement(enemyInLine, 2, 0);

        int score = decider.evalCellDangerosity(new Coordinate(0, 0), new Coordinate(0, 1), movingPawn);

        assertEquals(800, score);
    }

    @Test
    public void testEvalCellDangerosity_WhenSafeReturnsZero() {
        int score = decider.evalCellDangerosity(new Coordinate(0, 0), new Coordinate(0, 1), movingPawn);

        assertEquals(0, score);
    }

    @Test
    public void testEvalCellFavorability_WhenCapturePossiblePositive() {
        Soldier enemy = new Soldier(Team.MUSCOVITES, stage);
        board.addElement(enemy, 0, 2);

        int score = decider.evalCellFavorability(new Coordinate(0, 0), new Coordinate(0, 1), movingPawn);

        assertTrue(score > 0);
    }

    @Test
    public void testIsBetweenKingAndBorders_WhenBetweenReturnsTrue() {
        boolean result = decider.isBetweenKingAndBorders(new Coordinate(4, 6), 4, 4);

        assertTrue(result);
    }

    @Test
    public void testIsBetweenKingAndBorders_WhenNotBetweenReturnsFalse() {
        boolean result = decider.isBetweenKingAndBorders(new Coordinate(5, 6), 4, 4);

        assertFalse(result);
    }

    @Test
    public void testIsBetweenKingAndWinCoords_WhenBetweenReturnsTrue() {
        boolean result = decider.isBetweenKingAndWinCoords(new Coordinate(4, 6), 4, 4);

        assertTrue(result);
    }

    @Test
    public void testCanBeCapturedNextTurn_WhenEnemyCanCapture() {
        Soldier enemyAdjacent = new Soldier(Team.MUSCOVITES, stage);
        Soldier enemyInLine = new Soldier(Team.MUSCOVITES, stage);

        board.addElement(enemyAdjacent, 0, 2);
        board.addElement(enemyInLine, 2, 0);

        Coordinate source = new Coordinate(0, 0);
        Coordinate dest = new Coordinate(0, 1);

        boolean result = decider.canBeCapturedNextTurn(source, dest, movingPawn);

        assertTrue(result);
    }

    @Test
    public void testCanBeCapturedNextTurn_WhenNoAdjacentEnemy() {
        Soldier enemyFar = new Soldier(Team.MUSCOVITES, stage);
        board.addElement(enemyFar, 2, 0);

        Coordinate source = new Coordinate(0, 0);
        Coordinate dest = new Coordinate(0, 1);

        boolean result = decider.canBeCapturedNextTurn(source, dest, movingPawn);

        assertFalse(result);
    }

    @Test
    public void testCountCapturableNextTurn_WhenEnemyVisibleAcross() {
        Soldier enemyAdjacent = new Soldier(Team.MUSCOVITES, stage);
        Soldier enemyVisible = new Soldier(Team.MUSCOVITES, stage);
        board.addElement(enemyAdjacent, 4, 5);
        board.addElement(enemyVisible, 0, 8);

        int result = decider.countCapturableNextTurn(new Coordinate(4, 4), movingPawn);

        assertEquals(1, result);
    }

    @Test
    public void testAddMoveToHistory_AndComputeRepetitiveScore() {
        for (int i = 0; i < 64; i++) {
            Move dummy = new Move(1000 + i, 1000 + i, 1001 + i, 1002 + i);
            decider.addMoveToHistory(dummy);
        }

        Move p1 = new Move(2000, 2001, 2002, 2003);
        Move p2 = new Move(2010, 2011, 2012, 2013);
        Move p3 = new Move(2020, 2021, 2022, 2023);
        Move p4 = new Move(2030, 2031, 2032, 2033);

        decider.addMoveToHistory(p4);
        decider.addMoveToHistory(p3);
        decider.addMoveToHistory(p2);
        decider.addMoveToHistory(p1);
        decider.addMoveToHistory(p4);
        decider.addMoveToHistory(p3);
        decider.addMoveToHistory(p2);
        decider.addMoveToHistory(p1);

        int result = decider.computeRepetitiveScore();

        assertEquals(5, result);
    }

    @Test
    public void testIsNextToKing_WhenAdjacentTrue() {
        boolean result = decider.isNextToKing(new Coordinate(4, 5), new Coordinate(4, 4));

        assertTrue(result);
    }

    @Test
    public void testIsNextToKing_WhenNotAdjacentFalse() {
        boolean result = decider.isNextToKing(new Coordinate(6, 6), new Coordinate(4, 4));

        assertFalse(result);
    }
}
