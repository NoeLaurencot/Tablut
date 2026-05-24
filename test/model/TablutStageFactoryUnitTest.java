package model;

import boardifier.model.Model;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TablutStageFactoryUnitTest {

    @Test
    public void testCreateStage_ShouldReturnStageModel() {
        Model model = Mockito.mock(Model.class);
        TablutStageFactory factory = new TablutStageFactory();

        TablutStageModel stage = factory.createStage("tablut", model);

        assertNotNull(stage);
        assertEquals("tablut", stage.getName());
    }

    @Test
    public void testCreateStage_ShouldCreateBoard() {
        Model model = Mockito.mock(Model.class);
        TablutStageFactory factory = new TablutStageFactory();

        TablutStageModel stage = factory.createStage("tablut", model);

        assertNotNull(stage.getBoard());
        assertTrue(stage.getBoard() instanceof TablutBoard);
    }

}