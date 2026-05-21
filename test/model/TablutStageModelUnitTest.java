package model;

import boardifier.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

public class TablutStageModelUnitTest {

    TablutStageModel tablutStageModel;
    Model model;


    @BeforeEach
    public void init() {
        model = Mockito.mock(Model.class);
        tablutStageModel = new TablutStageModel("tablut",model);
    }

}
