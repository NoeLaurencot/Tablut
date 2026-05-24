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
}