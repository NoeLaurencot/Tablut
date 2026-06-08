package control;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class OptionMenuUnitTest {

    private OptionMenu optionMenu;
    private TablutStageModel tablutStageModel;

    @BeforeEach
    public void setup() {
        optionMenu = new OptionMenu();
        tablutStageModel = mock(TablutStageModel.class);
    }

    @Test
    public void testReadMenu_WhenEmptyInput_ReturnsFalse() {
        InputStream inputStream = new ByteArrayInputStream("".getBytes());

        boolean result = optionMenu.readMenu(tablutStageModel, inputStream);

        assertFalse(result);
        verifyNoInteractions(tablutStageModel);
    }

    @Test
    public void testReadMenu_WhenSave_ReturnsTrue() {
        InputStream inputStream = new ByteArrayInputStream("save".getBytes());

        boolean result = optionMenu.readMenu(tablutStageModel, inputStream);

        assertTrue(result);
        verifyNoInteractions(tablutStageModel);
    }

    @Test
    public void testReadMenu_WhenInvalidLength_ThenEmpty_ReturnsFalse() {
        InputStream inputStream = new ByteArrayInputStream("abc".getBytes());

        boolean result = optionMenu.readMenu(tablutStageModel, inputStream);

        assertFalse(result);
        verifyNoInteractions(tablutStageModel);
    }

    @Test
    public void testReadMenu_WhenOutOfBounds_ThenEmpty_ReturnsFalse() {
        InputStream inputStream = new ByteArrayInputStream("0A 9Z".getBytes());

        boolean result = optionMenu.readMenu(tablutStageModel, inputStream);

        assertFalse(result);
        verifyNoInteractions(tablutStageModel);
    }

    @Test
    public void testReadMenu_WhenValidKingMoveOptionLowerCase_ReturnsFalse() {
        InputStream inputStream = new ByteArrayInputStream("1a".getBytes());

        boolean result = optionMenu.readMenu(tablutStageModel, inputStream);

        assertFalse(result);
        verify(tablutStageModel).setKingMoveMode(model.OptionMenu.KING_MOVE_MODE[0]);
    }

    @Test
    public void testReadMenu_WhenValidWinModeOptionUpperCase_ReturnsFalse() {
        InputStream inputStream = new ByteArrayInputStream("2B".getBytes());

        boolean result = optionMenu.readMenu(tablutStageModel, inputStream);

        assertFalse(result);
        verify(tablutStageModel).setWinMode(model.OptionMenu.WIN_MODE[1]);
    }

    @Test
    public void testReadMenu_WhenValidBoundsButNoPrefixMatch_ReturnsFalse() {
        InputStream inputStream = new ByteArrayInputStream("3A".getBytes());

        boolean result = optionMenu.readMenu(tablutStageModel, inputStream);

        assertFalse(result);
        verifyNoInteractions(tablutStageModel);
    }
}