package model;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

    }
}
