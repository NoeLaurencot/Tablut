package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TablutBoardUnitTest {


    TablutStageModel tablutStageModel;
    TablutBoard tablutBoard;


    @BeforeEach
    public void init() {
        tablutStageModel = Mockito.mock(TablutStageModel.class);
        tablutBoard = new TablutBoard(0,0,tablutStageModel);
    }

}
