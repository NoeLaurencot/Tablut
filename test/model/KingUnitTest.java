package model;

import boardifier.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KingUnitTest {

    TablutStageModel tablutStageModel;
    TablutBoard tablutBoard;
    King king;
    Model model;

    @BeforeEach
    public void init() {

        model = Mockito.mock(Model.class);

        tablutStageModel = new TablutStageModel("tablut", model);

        tablutBoard = new TablutBoard(0, 0, tablutStageModel);
        tablutStageModel.setBoard(tablutBoard);


        tablutStageModel.setFortresses(new Fortress[0]);

        king = new King(tablutStageModel);

        tablutBoard.addElement(king, 4, 4);
    }

    @Test
    public void testShouldDie_WhenSurroundedBy4Enemies() {

        Soldier e1 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e2 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e3 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e4 = new Soldier(Team.MUSCOVITES, tablutStageModel);

        tablutBoard.addElement(e1, 4, 5); // droite
        tablutBoard.addElement(e2, 5, 4); // bas
        tablutBoard.addElement(e3, 3, 4); // haut
        tablutBoard.addElement(e4, 4, 3); // gauche

        boolean result = king.shouldDie();

        assertTrue(result);
    }

    @Test
    public void testShouldNotDie_WhenOnly3Enemies() {
        Soldier e1 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e2 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e3 = new Soldier(Team.MUSCOVITES, tablutStageModel);

        tablutBoard.addElement(e1, 4, 5);
        tablutBoard.addElement(e2, 5, 4);
        tablutBoard.addElement(e3, 3, 4);


        boolean result = king.shouldDie();

        assertFalse(result);
    }

    @Test
    public void testShouldNotDie_WhenNearFortressAndOnly2Enemies() {

        Fortress fortress = Mockito.mock(Fortress.class);

        Mockito.when(fortress.getX()).thenReturn(4.0);
        Mockito.when(fortress.getY()).thenReturn(3.0);

        tablutStageModel.setFortresses(new Fortress[]{fortress});

        Soldier e1 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e2 = new Soldier(Team.MUSCOVITES, tablutStageModel);

        tablutBoard.addElement(e1, 4, 5);
        tablutBoard.addElement(e2, 5, 4);

        boolean result = king.shouldDie();

        assertFalse(result);
    }

    @Test
    public void testKingPosition_WhenKingOnBorder() {

        tablutBoard.reset();

        tablutBoard.addElement(king, 0, 4);

        Coordinate position = tablutBoard.getKingPosition();

        assertEquals(0, position.getX());
        assertEquals(4, position.getY());
    }

    @Test
    public void testGetLegalMoves_LimitlessFromCenter() {
        tablutStageModel.setKingMoveMode(KingMoveMode.LIMITLESS);

        List<Coordinate> moves = king.getLegalMoves();

        assertEquals(16, moves.size());
    }

    @Test
    public void testGetLegalMoves_LimitedFromCenter() {
        tablutStageModel.setKingMoveMode(KingMoveMode.LIMITED);

        List<Coordinate> moves = king.getLegalMoves();

        assertEquals(4, moves.size());
    }

    @Test
    public void testIsSurrounded_WhenEnemiesOnAllSides() {
        Soldier e1 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e2 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e3 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e4 = new Soldier(Team.MUSCOVITES, tablutStageModel);

        tablutBoard.addElement(e1, 4, 5);
        tablutBoard.addElement(e2, 5, 4);
        tablutBoard.addElement(e3, 3, 4);
        tablutBoard.addElement(e4, 4, 3);

        assertTrue(king.isSurrounded());
    }

    @Test
    public void testWouldBeSurroundedIfEnemyAt_WhenThreeEnemies() {
        Soldier e1 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e2 = new Soldier(Team.MUSCOVITES, tablutStageModel);
        Soldier e3 = new Soldier(Team.MUSCOVITES, tablutStageModel);

        tablutBoard.addElement(e1, 4, 5);
        tablutBoard.addElement(e2, 5, 4);
        tablutBoard.addElement(e3, 3, 4);

        assertTrue(king.wouldBeSurroundedIfEnemyAt(new Coordinate(4, 3)));
    }
}