package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TablutBoardUnitTest {

    TablutStageModel tablutStageModel;
    TablutBoard tablutBoard;

    @BeforeEach
    public void init() {
        tablutStageModel = Mockito.mock(TablutStageModel.class);
        tablutBoard = new TablutBoard(0, 0, tablutStageModel);
        Mockito.when(tablutStageModel.getBoard()).thenReturn(tablutBoard);
        Mockito.when(tablutStageModel.isFortressAt(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);
    }

    @Test
    public void testGetWinCoords_ShouldContainBorders() {
        var wins = tablutBoard.getWinCoords();

        assertTrue(wins.contains(new Coordinate(0, 0)));
        assertTrue(wins.contains(new Coordinate(8, 0)));
        assertTrue(wins.contains(new Coordinate(0, 8)));
        assertTrue(wins.contains(new Coordinate(8, 8)));
    }

    @Test
    public void testSetWinCoords_ShouldReplaceList() {
        var wins = new java.util.ArrayList<Coordinate>();
        wins.add(new Coordinate(4, 4));

        tablutBoard.setWinCoords(wins);

        assertEquals(wins, tablutBoard.getWinCoords());
    }

    @Test
    public void testGetKingPosition_ShouldReturnCorrectPosition() {
        King king = new King(tablutStageModel);
        tablutBoard.addElement(king, 4, 4);

        Coordinate pos = tablutBoard.getKingPosition();

        assertEquals(4, pos.getX());
        assertEquals(4, pos.getY());
    }

    @Test
    public void testGetKingPosition_ShouldReturnMinusOne_WhenNoKing() {
        Coordinate pos = tablutBoard.getKingPosition();

        assertEquals(-1, pos.getX());
        assertEquals(-1, pos.getY());
    }

    @Test
    public void testSetValidCells_ShouldMarkReachableCells() {
        Soldier soldier = new Soldier(Team.SWEDISH, tablutStageModel);
        tablutBoard.addElement(soldier, 4, 4);

        tablutBoard.setValidCells(4, 4);

        assertTrue(tablutBoard.canReachCell(4, 5));
    }

    @Test
    public void testComputeAllValidCells_ShouldReturnMovesForTeam() {
        Soldier soldier = new Soldier(Team.SWEDISH, tablutStageModel);
        tablutBoard.addElement(soldier, 4, 4);

        var moves = tablutBoard.computeAllValidCells(Team.SWEDISH);

        assertFalse(moves.isEmpty());
    }

    @Test
    public void testComputeValidCells_ShouldReturnMovesForPawn() {
        Soldier soldier = new Soldier(Team.SWEDISH, tablutStageModel);
        tablutBoard.addElement(soldier, 4, 4);

        var moves = tablutBoard.computeValidCells(4, 4);

        assertFalse(moves.isEmpty());
    }

    @Test
    public void testIsStalemate_WhenNoMoves() {
        Soldier soldier = new Soldier(Team.SWEDISH, tablutStageModel);
        Soldier e1 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e2 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e3 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e4 = new Soldier(Team.MUSCOVITES, tablutStageModel);

        tablutBoard.addElement(soldier, 4, 4);
        tablutBoard.addElement(e1, 4, 5);
        tablutBoard.addElement(e2, 5, 4);
        tablutBoard.addElement(e3, 3, 4);
        tablutBoard.addElement(e4, 4, 3);

        assertTrue(tablutBoard.isStalemate(Team.SWEDISH));
    }

    @Test
    public void testAreSwedishWinning_WhenKingOnBorder() {
        King king = new King(tablutStageModel);
        tablutBoard.addElement(king, 0, 4);

        assertTrue(tablutBoard.areSwedishWinning());
    }

    @Test
    public void testAreSwedishWinning_WhenKingNotOnBorder() {
        King king = new King(tablutStageModel);
        tablutBoard.addElement(king, 4, 4);

        assertFalse(tablutBoard.areSwedishWinning());
    }

    @Test
    public void testAreMuscovitesWinning_WhenKingDead() {
        assertTrue(tablutBoard.areMuscovitesWinning());
    }

    @Test
    public void testAreMuscovitesWinning_WhenKingAlive() {
        King king = new King(tablutStageModel);
        tablutBoard.addElement(king, 4, 4);

        assertFalse(tablutBoard.areMuscovitesWinning());
    }

    @Test
    public void testConvertGridToInt_ShouldReturnCorrectValues() {
        Soldier swedish = new Soldier(Team.SWEDISH, tablutStageModel);
        Soldier muscovite = new Soldier(Team.MUSCOVITES, tablutStageModel);
        King king = new King(tablutStageModel);

        tablutBoard.addElement(swedish, 0, 0);
        tablutBoard.addElement(muscovite, 1, 1);
        tablutBoard.addElement(king, 2, 2);

        short[][] map = tablutBoard.convertGridToInt();

        assertEquals(1, map[0][0]);
        assertEquals(2, map[1][1]);
        assertEquals(3, map[2][2]);
        assertEquals(0, map[3][3]);
    }

    @Test
    public void testGetGameStage_ShouldReturnStageModel() {
        assertSame(tablutStageModel, tablutBoard.getGameStage());
    }

    @Test
    public void testSetGrid_ShouldCreatePieces() {
        short[][] grid = new short[9][9];
        grid[0][0] = 1;
        grid[1][1] = 2;
        grid[2][2] = 3;

        tablutBoard.setGrid(grid);

        assertTrue(tablutBoard.getElement(0, 0) instanceof Soldier);
        assertTrue(((Soldier) tablutBoard.getElement(0, 0)).getTeam() == Team.SWEDISH);
        assertTrue(tablutBoard.getElement(1, 1) instanceof Soldier);
        assertTrue(((Soldier) tablutBoard.getElement(1, 1)).getTeam() == Team.MUSCOVITES);
        assertTrue(tablutBoard.getElement(2, 2) instanceof King);
    }

    @Test
    public void testFindKing_ShouldReturnKing() {
        King king = new King(tablutStageModel);
        tablutBoard.addElement(king, 4, 4);

        assertSame(king, tablutBoard.findKing());
    }

    @Test
    public void testOutOfBounds_ShouldDetectInvalidCoords() {
        assertTrue(tablutBoard.outOfBounds(new Coordinate(-1, 0)));
        assertFalse(tablutBoard.outOfBounds(new Coordinate(0, 0)));
    }

    @Test
    public void testCanSeeCoordinate_ShouldSeeBorder() {
        assertTrue(tablutBoard.canSeeCoordinate(new Coordinate(4, 4), new Coordinate(4, 8)));
    }

    @Test
    public void testCanSeeKing_ShouldReturnTrueWhenAligned() {
        King king = new King(tablutStageModel);
        tablutBoard.addElement(king, 4, 8);

        assertTrue(tablutBoard.canSeeKing(new Coordinate(4, 0)));
    }

    @Test
    public void testGetAdjacentCoords_OnCorner() {
        var coords = tablutBoard.getAdjacentCoords(new Coordinate(0, 0));

        assertEquals(2, coords.size());
        assertTrue(coords.contains(new Coordinate(1, 0)));
        assertTrue(coords.contains(new Coordinate(0, 1)));
    }

    @Test
    public void testGetAdjacentEnemyPawnsCoords_ReturnsEnemiesOnly() {
        Soldier ally = new Soldier(Team.SWEDISH, tablutStageModel);
        Soldier enemy = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier ally2 = new Soldier(Team.SWEDISH, tablutStageModel);
        tablutBoard.addElement(ally, 4, 4);
        tablutBoard.addElement(enemy, 4, 5);
        tablutBoard.addElement(ally2, 4, 3);

        var enemies = tablutBoard.getAdjacentEnemyPawnsCoords(new Coordinate(4, 4), Team.SWEDISH);

        assertEquals(1, enemies.size());
        assertTrue(enemies.contains(new Coordinate(4, 5)));
    }

    @Test
    public void testGetFarthestVisibleCoord_StopsAtBlocker() {
        Soldier blocker = new Soldier(Team.SWEDISH, tablutStageModel);
        tablutBoard.addElement(blocker, 0, 3);

        Coordinate far = tablutBoard.getFarthestVisibleCoord(new Coordinate(0, 0), new int[]{0, 1});

        assertEquals(new Coordinate(0, 3), far);
    }

    @Test
    public void testGetFarthestVisibleCoords_ReturnsExpectedCells() {
        Soldier blocker = new Soldier(Team.SWEDISH, tablutStageModel);
        tablutBoard.addElement(blocker, 0, 3);

        var coords = tablutBoard.getFarthestVisibleCoords(new Coordinate(0, 0));

        assertTrue(coords.contains(new Coordinate(0, 3)));
        assertTrue(coords.contains(new Coordinate(8, 0)));
    }

    @Test
    public void testGetFarthestVisibleEnemyCoords_ReturnsEnemies() {
        Soldier enemy = new Soldier(Team.MUSCOVITES, tablutStageModel);
        tablutBoard.addElement(enemy, 0, 3);

        var enemies = tablutBoard.getFarthestVisibleEnemyCoords(new Coordinate(0, 0), Team.SWEDISH);

        assertEquals(1, enemies.size());
        assertTrue(enemies.contains(new Coordinate(0, 3)));
    }

    @Test
    public void testIsAnyEnemyVisible_ReturnsTrue() {
        Soldier enemy = new Soldier(Team.MUSCOVITES, tablutStageModel);
        tablutBoard.addElement(enemy, 0, 3);

        assertTrue(tablutBoard.isAnyEnemyVisible(new Coordinate(0, 0), Team.SWEDISH));
    }

    @Test
    public void testWouldBeSurroundedIfEnemyAt_ForKing() {
        King king = new King(tablutStageModel);
        Soldier e1 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e2 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e3 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        tablutBoard.addElement(king, 4, 4);
        tablutBoard.addElement(e1, 4, 5);
        tablutBoard.addElement(e2, 5, 4);
        tablutBoard.addElement(e3, 3, 4);

        assertTrue(tablutBoard.wouldBeSurroundedIfEnemyAt(new Coordinate(4, 4), new Coordinate(4, 3), king));
    }

    @Test
    public void testWouldBeSurroundedIfEnemyAt_ForSoldier() {
        Soldier soldier = new Soldier(Team.SWEDISH, tablutStageModel);
        Soldier enemy = new Soldier(Team.MUSCOVITES, tablutStageModel);
        tablutBoard.addElement(soldier, 4, 4);
        tablutBoard.addElement(enemy, 4, 5);

        assertTrue(tablutBoard.wouldBeSurroundedIfEnemyAt(new Coordinate(4, 4), new Coordinate(4, 3), soldier));
    }

    @Test
    public void testGetEmptyNeighbors_ReturnsOnlyEmpties() {
        Soldier blocker1 = new Soldier(Team.SWEDISH, tablutStageModel);
        Soldier blocker2 = new Soldier(Team.SWEDISH, tablutStageModel);
        tablutBoard.addElement(blocker1, 4, 5);
        tablutBoard.addElement(blocker2, 5, 4);

        var empty = tablutBoard.getEmptyNeighbors(new Coordinate(4, 4));

        assertEquals(2, empty.size());
        assertTrue(empty.contains(new Coordinate(3, 4)));
        assertTrue(empty.contains(new Coordinate(4, 3)));
    }

    @Test
    public void testCountEnemyNeighbors_ReturnsCount() {
        Soldier enemy1 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier enemy2 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        tablutBoard.addElement(enemy1, 4, 5);
        tablutBoard.addElement(enemy2, 5, 4);

        int count = tablutBoard.countEnemyNeighbors(new Coordinate(4, 4), Team.SWEDISH);

        assertEquals(2, count);
    }

    @Test
    public void testCaptureCount_WhenEnemyWouldBeCaptured() {
        Soldier ally = new Soldier(Team.SWEDISH, tablutStageModel);
        Soldier enemy = new Soldier(Team.MUSCOVITES, tablutStageModel);
        tablutBoard.addElement(ally, 4, 6);
        tablutBoard.addElement(enemy, 4, 5);

        int count = tablutBoard.captureCount(new Coordinate(4, 4), Team.SWEDISH);

        assertEquals(1, count);
    }

    @Test
    public void testCountVisibleBorder_FromCenter() {
        int count = tablutBoard.countVisibleBorder(new Coordinate(4, 4));

        assertEquals(4, count);
    }
}