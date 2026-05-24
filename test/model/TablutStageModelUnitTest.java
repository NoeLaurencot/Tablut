package model;

import boardifier.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TablutStageModelUnitTest {

    TablutStageModel tablutStageModel;
    Model model;


    @BeforeEach
    public void init() {
        model = Mockito.mock(Model.class);
        tablutStageModel = new TablutStageModel("tablut",model);
    }

    @Test
    public void testConstructor_ShouldSetName() {
        assertEquals("tablut", tablutStageModel.getName());
    }

    @Test
    public void testSetBoard_ShouldStoreBoard() {
        TablutBoard board = new TablutBoard(0, 0, tablutStageModel);
        tablutStageModel.setBoard(board);

        assertEquals(board, tablutStageModel.getBoard());
    }

    @Test
    public void testGetBoard_WhenNoBoard_ShouldReturnNull() {
        assertNull(tablutStageModel.getBoard());
    }
}