package model;


import boardifier.model.GameStageModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class KingUnitTest {

    King king;
    TablutStageModel tablutStageModel;
    TablutBoard tablutBoard;


    @BeforeEach
    public void init() {
        tablutStageModel = Mockito.mock(TablutStageModel.class);
        king = new King(tablutStageModel);
        tablutBoard = Mockito.mock(TablutBoard.class);
        king.setContainer(tablutBoard);
    }

    @Test
    public void testShouldDie() {

    }
}
