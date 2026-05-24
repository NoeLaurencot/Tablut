package control;

import boardifier.model.Model;
import boardifier.model.TextElement;
import boardifier.view.View;
import model.AIMode;
import model.Fortress;
import model.King;
import model.MenuElement;
import model.Soldier;
import model.TablutBoard;
import model.TablutStageModel;
import model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TablutControllerUnitTest {

	Model model;
	View view;
	TablutStageModel stage;
	TablutBoard board;
	TablutController controller;

	@BeforeEach
	public void init() {
		model = new Model();
		view = Mockito.mock(View.class);
		stage = new TablutStageModel("tablut", model);
		board = new TablutBoard(0, 0, stage);
		stage.setBoard(board);
		stage.setFortresses(new Fortress[0]);
		model.setGameStage(stage);

		controller = new TablutController(model, view);
	}

	@Test
	public void testConstructor_CreatesController() {
		TablutController created = new TablutController(model, view);

		assertNotNull(created);
	}

	@Test
	public void testAnalyseAndPlay_ValidMoveMovesPawn() {
		model.addHumanPlayer("p1");
		model.addHumanPlayer("p2");
		model.setIdPlayer(0);

		Soldier pawn = new Soldier(Team.SWEDISH, stage);
		board.addElement(pawn, 0, 0);

		boolean result = controller.analyseAndPlay("A1A2");

		assertTrue(result);
		assertSame(board.getElement(1, 0), pawn);
	}

	@Test
	public void testAnalyseAndPlay_InvalidMoveReturnsFalse() {
		model.addHumanPlayer("p1");
		model.addHumanPlayer("p2");
		model.setIdPlayer(0);

		Soldier pawn = new Soldier(Team.SWEDISH, stage);
		board.addElement(pawn, 0, 0);

		boolean result = controller.analyseAndPlay("A1B2");

		assertFalse(result);
		assertSame(board.getElement(0, 0), pawn);
	}

	@Test
	public void testPlayTurn_HumanInputMovesPawn() {
		model.addHumanPlayer("p1");
		model.setIdPlayer(0);

		Soldier pawn = new Soldier(Team.SWEDISH, stage);
		board.addElement(pawn, 0, 0);

		BufferedReader reader = new BufferedReader(new StringReader("A1A2\n"));
		controller.playTurn(reader);

		assertSame(board.getElement(1, 0), pawn);
	}

	@Test
	public void testPlayTurn_ComputerMovesSingleOption() {
		model.addComputerPlayer("cpu0");
		model.addComputerPlayer("cpu1");
		model.setIdPlayer(1);
		stage.setAiModeSingle(AIMode.SIMPLE_EVAL);

		King king = new King(stage);
		board.addElement(king, 4, 4);

		Soldier pawn = new Soldier(Team.MUSCOVITES, stage);
		Soldier blockerRight = new Soldier(Team.SWEDISH, stage);
		Soldier blockerDown = new Soldier(Team.SWEDISH, stage);
		board.addElement(pawn, 0, 0);
		board.addElement(blockerRight, 0, 1);
		board.addElement(blockerDown, 2, 0);

		BufferedReader reader = new BufferedReader(new StringReader(""));
		controller.playTurn(reader);

		assertSame(pawn, board.getElement(1, 0));
	}

	@Test
	public void testEndOfTurn_SetsNextPlayerAndUpdatesName() {
		model.addHumanPlayer("p1");
		model.addHumanPlayer("p2");
		model.setIdPlayer(0);

		TextElement playerName = new TextElement("p1", stage);
		stage.setPlayerName(playerName);

		controller.endOfTurn();

		assertEquals(1, model.getIdPlayer());
		assertEquals("p2", stage.getPlayerName().getText());
	}

	@Test
	public void testAfterPlay_CapturesEnemy() {
		Soldier pawn = new Soldier(Team.SWEDISH, stage);
		Soldier enemy = new Soldier(Team.MUSCOVITES, stage);
		Soldier ally = new Soldier(Team.SWEDISH, stage);
		board.addElement(pawn, 4, 4);
		board.addElement(enemy, 4, 5);
		board.addElement(ally, 4, 6);

		controller.afterPlay(4, 4);

		assertTrue(board.isEmptyAt(4, 5));
	}

	@Test
	public void testDisplayAndApplyMenu_UsesProvidedOptionMenu() {
		MenuElement menuElement = new MenuElement(stage, "");
		stage.setMenuElement(menuElement);

		OptionMenu optionMenu = Mockito.mock(OptionMenu.class);
		Mockito.when(optionMenu.readMenu(stage)).thenReturn(true);

		TestableController testController = new TestableController(model, view);
		testController.setOptionMenu(optionMenu);

		boolean result = testController.displayAndApplyMenu(optionMenu);

		assertTrue(result);
		assertFalse(menuElement.isVisible());
		Mockito.verify(optionMenu).readMenu(stage);
	}

	@Test
	public void testDisplayAndApplyMenu_NoArgUsesFactory() {
		MenuElement menuElement = new MenuElement(stage, "");
		stage.setMenuElement(menuElement);

		OptionMenu optionMenu = Mockito.mock(OptionMenu.class);
		Mockito.when(optionMenu.readMenu(stage)).thenReturn(true);

		TestableController testController = new TestableController(model, view);
		testController.setOptionMenu(optionMenu);

		boolean result = testController.displayAndApplyMenu();

		assertTrue(result);
		assertFalse(menuElement.isVisible());
		Mockito.verify(optionMenu).readMenu(stage);
	}

	@Test
	public void testOpenMenu_UsesProvidedOptionMenu() {
		OptionMenu optionMenu = Mockito.mock(OptionMenu.class);
		Mockito.when(optionMenu.readMenu(stage)).thenReturn(true);

		TestableController testController = new TestableController(model, view);
		testController.setOptionMenu(optionMenu);

		testController.openMenu(optionMenu);

		Mockito.verify(optionMenu).readMenu(stage);
	}

	@Test
	public void testOpenMenu_NoArgUsesFactory() {
		OptionMenu optionMenu = Mockito.mock(OptionMenu.class);
		Mockito.when(optionMenu.readMenu(stage)).thenReturn(true);

		TestableController testController = new TestableController(model, view);
		testController.setOptionMenu(optionMenu);

		testController.openMenu();

		Mockito.verify(optionMenu).readMenu(stage);
	}

	@Test
	public void testStageLoop_StopsAfterPlay() {
		model.addHumanPlayer("p1");
		model.setIdPlayer(0);
		stage.setMode(0);

		StageLoopController loopController = new StageLoopController(model, view);

		loopController.stageLoop(new ByteArrayInputStream(new byte[0]));

		assertTrue(loopController.playCalled);
		assertTrue(loopController.endGameCalled);
	}

	static class TestableController extends TablutController {
		private OptionMenu optionMenu;

		public TestableController(Model model, View view) {
			super(model, view);
		}

		public void setOptionMenu(OptionMenu optionMenu) {
			this.optionMenu = optionMenu;
		}

		@Override
		public void update() {
		}

		@Override
		protected OptionMenu createOptionMenu() {
			if (optionMenu != null) {
				return optionMenu;
			}
			return super.createOptionMenu();
		}
	}

	static class StageLoopController extends TablutController {
		boolean playCalled;
		boolean endGameCalled;

		public StageLoopController(Model model, View view) {
			super(model, view);
		}

		@Override
		public void update() {
		}

		@Override
		public void playTurn(BufferedReader input) {
			playCalled = true;
			model.stopStage();
		}

		@Override
		public void endOfTurn() {
		}

		@Override
		public void endGame() {
			endGameCalled = true;
		}
	}
}
