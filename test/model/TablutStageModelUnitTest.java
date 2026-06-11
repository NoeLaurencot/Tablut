package model;

import boardifier.model.Model;
import boardifier.model.TextElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

import model.AIMode;
import model.Coordinate;
import model.Fortress;
import model.King;
import model.KingMoveMode;
import model.MenuElement;
import model.Pawn;
import model.Soldier;
import model.WinMode;

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

    @Test
    public void testSetFortresses_ShouldStoreFortresses() {
        Fortress[] forts = new Fortress[]{Mockito.mock(Fortress.class)};
        tablutStageModel.setFortresses(forts);

        assertEquals(forts, tablutStageModel.getFortresses());
    }

    @Test
    public void testSetPlayerName_ShouldStorePlayerName() {
        TextElement text = new TextElement("p1", tablutStageModel);
        tablutStageModel.setPlayerName(text);

        assertEquals(text, tablutStageModel.getPlayerName());
    }

    @Test
    public void testSetMenuElement_ShouldStoreMenuElement() {
        MenuElement menu = new MenuElement(tablutStageModel, "");
        tablutStageModel.setMenuElement(menu);

        assertEquals(menu, tablutStageModel.getMenuElement());
    }

    @Test
    public void testSetWinMode_ShouldUpdateWinCoords() {
        TablutBoard board = new TablutBoard(0, 0, tablutStageModel);
        tablutStageModel.setBoard(board);

        tablutStageModel.setWinMode(WinMode.CORNERS);

        assertEquals(4, board.getWinCoords().size());
        assertTrue(board.getWinCoords().contains(new Coordinate(0, 0)));
        assertTrue(board.getWinCoords().contains(new Coordinate(8, 0)));
        assertTrue(board.getWinCoords().contains(new Coordinate(0, 8)));
        assertTrue(board.getWinCoords().contains(new Coordinate(8, 8)));
    }

    @Test
    public void testSetKingMoveMode_ShouldStoreMode() {
        tablutStageModel.setKingMoveMode(KingMoveMode.LIMITED);

        assertEquals(KingMoveMode.LIMITED, TablutOptionModel.getKingMode());
    }

    @Test
    public void testSetAiModeSingle_ShouldStoreMode() {
        tablutStageModel.setAiModeSingle(AIMode.SIMPLE_EVAL);

        assertEquals(AIMode.SIMPLE_EVAL, tablutStageModel.getAiModeSingle());
    }

    @Test
    public void testSetAiModeMultiple_ShouldStoreModes() {
        AIMode[] modes = new AIMode[]{AIMode.MINMAX, AIMode.SIMPLE_EVAL};
        tablutStageModel.setAiModeMultiple(modes);

        assertEquals(modes, tablutStageModel.getAiModeMultiple());
    }

    @Test
    public void testSetMode_ShouldStoreMode() {
        tablutStageModel.setMode(2);

        assertEquals(2, tablutStageModel.getMode());
    }

    @Test
    public void testSetPawns_ShouldAddToBoard() {
        TablutBoard board = new TablutBoard(0, 0, tablutStageModel);
        tablutStageModel.setBoard(board);

        Pawn[] pawns = new Pawn[2];
        pawns[0] = new Soldier(0, 0, Team.SWEDISH, tablutStageModel);
        pawns[1] = new King(4, 4, tablutStageModel);

        tablutStageModel.setPawns(pawns);

        assertEquals(pawns, tablutStageModel.getPawns());
        assertTrue(board.getElement(0, 0) instanceof Soldier);
        assertTrue(board.getElement(4, 4) instanceof King);
    }
}