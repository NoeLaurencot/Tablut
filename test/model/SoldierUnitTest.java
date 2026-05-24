package model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SoldierUnitTest {

    Soldier soldierMuscovite;
    Soldier soldierSwedish;
    TablutStageModel tablutStageModel;
    TablutBoard tablutBoard;


    @BeforeEach
    public void init() {
        tablutStageModel = Mockito.mock(TablutStageModel.class);
        soldierMuscovite = new Soldier(Team.MUSCOVITES,tablutStageModel);
        soldierSwedish = new Soldier(Team.SWEDISH,tablutStageModel);

        tablutBoard = Mockito.mock(TablutBoard.class);
        soldierMuscovite.setContainer(tablutBoard);
        soldierSwedish.setContainer(tablutBoard);

    }

    @Test
    public void testShouldDie() {

        assertTrue(soldierMuscovite.shouldDie());
    }

    @Test
    public void testShouldDie_Swedish() {
        assertTrue(soldierSwedish.shouldDie());
    }

    @Test
    public void testGetLegalMoves_ShouldReturnMoves_WhenBoardIsEmpty() {

        TablutStageModel realStage = Mockito.mock(TablutStageModel.class);

        TablutBoard realBoard = new TablutBoard(0, 0, realStage);

        Mockito.when(realStage.isFortressAt(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);

        Soldier soldier = new Soldier(Team.SWEDISH, realStage);

        realBoard.addElement(soldier, 4, 4);

        List<Coordinate> moves = soldier.getLegalMoves();

        assertFalse(moves.isEmpty());
    }

    @Test
    public void testGetLegalMoves_ShouldBeBlockedByAnotherPawn() {

        TablutStageModel realStage = Mockito.mock(TablutStageModel.class);

        TablutBoard realBoard = new TablutBoard(0, 0, realStage);

        Mockito.when(realStage.isFortressAt(Mockito.anyInt(), Mockito.anyInt())).thenReturn(false);

        Soldier soldier = new Soldier(Team.SWEDISH, realStage);
        Soldier blocker = new Soldier(Team.MUSCOVITES, realStage);

        realBoard.addElement(soldier, 4, 4);
        realBoard.addElement(blocker, 4, 5);

        List<Coordinate> moves = soldier.getLegalMoves();

        assertFalse(moves.contains(new Coordinate(4, 5)));
    }

    @Test
    public void testGetLegalMoves_ShouldNotEnterFortress() {

        TablutStageModel realStage = Mockito.mock(TablutStageModel.class);

        TablutBoard realBoard = new TablutBoard(0, 0, realStage);

        Mockito.when(realStage.isFortressAt(4, 5)).thenReturn(true);

        Soldier soldier = new Soldier(Team.SWEDISH, realStage);

        realBoard.addElement(soldier, 4, 4);

        List<Coordinate> moves = soldier.getLegalMoves();

        assertFalse(moves.contains(new Coordinate(4, 5)));
    }
}