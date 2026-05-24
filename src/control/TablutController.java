package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import model.*;
import view.TablutStageView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TablutController extends Controller {

    BufferedReader consoleIn;
    boolean firstPlayer;

    public TablutController(Model model, View view) {
        super(model, view);
        firstPlayer = true;
    }

    /**
     * Defines what to do within the single stage of the single party
     * It is pretty straight forward to write :
     *
     * @param in InputStream
     */
    public void stageLoop(InputStream in) {
        consoleIn = new BufferedReader(new InputStreamReader(in));
        if (((TablutStageModel) model.getGameStage()).getMode() == 3)
            openMenu();
        update();
        while (!model.isEndStage()) {
            playTurn();
            endOfTurn();
            update();
        }
        endGame();
    }

    /**
     *
     */
    private void playTurn() {
        // get the new player
        Player p = model.getCurrentPlayer();
        if (p.getType() == Player.COMPUTER) {
            final long COOLDOWN = 100;
            AIMode aiModeUsed;
            Move bestMove = null;
            // To avoid lots of casting
            TablutStageModel stageModel = (TablutStageModel) model.getGameStage();
            System.out.println("COMPUTER PLAYS");
            ActionPlayer play;
            Decider decider;

            if (stageModel.getMode() == 3) {
                if (stageModel.getPlayerName().getText().equals("swedish computer")) {
                    switch (stageModel.getAiModeMultiple()[0]) {
                        case MINMAX:
                            decider = new TablutDecider(model, this);
                            aiModeUsed = AIMode.MINMAX;
                            break;
                        case SIMPLE_EVAL:
                            decider = new TablutSimpleDecider(model, this);
                            aiModeUsed = AIMode.SIMPLE_EVAL;
                            break;
                        default:
                            // Temporary
                            decider = new TablutDecider(model, this);
                            aiModeUsed = AIMode.MINMAX;
                            break;
                    }
                } else {
                    switch (stageModel.getAiModeMultiple()[1]) {
                        case MINMAX:
                            decider = new TablutDecider(model, this);
                            aiModeUsed = AIMode.MINMAX;
                            break;
                        case SIMPLE_EVAL:
                            decider = new TablutSimpleDecider(model, this);
                            aiModeUsed = AIMode.SIMPLE_EVAL;
                            break;
                        default:
                            // Temporary
                            decider = new TablutDecider(model, this);
                            aiModeUsed = AIMode.MINMAX;
                            break;
                    }
                }
            } else { // mode is only 1 computer
                switch (stageModel.getAiModeSingle()) {
                    case MINMAX:
                        decider = new TablutDecider(model, this);
                        aiModeUsed = AIMode.MINMAX;
                        break;
                    case SIMPLE_EVAL:
                        decider = new TablutSimpleDecider(model, this);
                        aiModeUsed = AIMode.SIMPLE_EVAL;
                        break;
                    default:
                        // Temporary
                        decider = new TablutDecider(model, this);
                        aiModeUsed = AIMode.MINMAX;
                        break;
                }
            }

            long timeElapsed = System.nanoTime();

            play = new ActionPlayer(model, this, decider, null);

            timeElapsed = System.nanoTime() - timeElapsed;

            if (timeElapsed < COOLDOWN) {
                try {
                    Thread.sleep(timeElapsed + (COOLDOWN - timeElapsed));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

//            ((TablutStageModel) model.getGameStage()).setKingMoveMode(KingMoveMode.LIMITED);
//            ((TablutStageModel) model.getGameStage()).setWinMode(WinMode.CORNERS);

            play.start();

            switch (aiModeUsed) {
                case MINMAX:
                    bestMove = ((TablutDecider) decider).bestMove;
                    afterPlay(bestMove.destX, bestMove.destY);
                    break;
                case SIMPLE_EVAL:
                    bestMove = ((TablutSimpleDecider) decider).bestMove;
                    afterPlay(bestMove.destX, bestMove.destY);
                    break;
            }

        } else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName() + " > ");
                try {
                    String line = consoleIn.readLine();

                    if (line.contains("stop")) {
                        System.exit(0);
                    }

                    if (line.length() == 4) {
                        ok = analyseAndPlay(line);
                    }

                    if (line.equals("options")) {
                        openMenu();
                        continue;
                    }

                    // temporary bypass cheat to test
                    // use Z as the first char to move a pawn anywhere without check
//                    if (line.charAt(0) == 'Z') {
//                        ActionList actions = ActionFactory.generateMoveWithinContainer(model, ((TablutStageModel) model.getGameStage()).getBoard().getElement((int) (line.charAt(2) - '1'), (int) (line.charAt(1) - 'a')), (int) (line.charAt(4) - '1'), (int) (line.charAt(3) - 'a'));
//                        actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
//                        ActionPlayer play = new ActionPlayer(model, this, actions);
//                        ok = true;
//                        play.start();
//                    }

                    if (!ok) {
                        System.out.println("incorrect instruction. retry!");
                    }
                } catch (IOException e) {
                }
            }
        }
    }

    public void endOfTurn() {


        model.setNextPlayer();
        // get the new player to display its name
        Player p = model.getCurrentPlayer();
        TablutStageModel stageModel = (TablutStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());
        /*
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (stageModel.getBoard().isEmptyAt(i, j)) {
                    System.out.print(1);
                } else {
                System.out.print(0);
                }
            }
            System.out.println();
        }*/
    }

    private boolean analyseAndPlay(String line) {
        TablutStageModel gameStage = (TablutStageModel) model.getGameStage();
        TablutBoard board = gameStage.getBoard();

        int sourceCol;
        int sourceRow;

        int destRow;
        int destCol;

        // get the coords for the source pawn in the board
        if (Character.isUpperCase(line.charAt(0))) {
            sourceCol = (int) (line.charAt(0) - 'A');
        } else {
            sourceCol = (int) (line.charAt(0) - 'a');
        }

        sourceRow = (int) (line.charAt(1) - '1');
        // check coords validity

        if ((sourceRow < 0) || (sourceRow > 8)) return false;
        if ((sourceCol < 0) || (sourceCol > 8)) return false;

        // get the ccords for the destination pawn in the board
        if (Character.isUpperCase(line.charAt(2))) {
            destCol = (int) (line.charAt(2) - 'A');
        } else {
            destCol = (int) (line.charAt(2) - 'a');
        }
        destRow = (int) (line.charAt(3) - '1');

        if (sourceCol != destCol && sourceRow != destRow) {
            return false;
        }

        // check coords validity
        if ((destRow < 0) || (destRow > 8)) return false;
        if ((destCol < 0) || (destCol > 8)) return false;
        if (board.isEmptyAt(sourceRow, sourceCol)) return false;
        Pawn pawn = (Pawn) board.getElement(sourceRow, sourceCol);
        if ((model.getIdPlayer() == 0) != (pawn.getTeam() == Team.SWEDISH)) return false;

        // compute valid cells for the chosen pawn
        gameStage.getBoard().setValidCells(sourceRow, sourceCol);
        if (!gameStage.getBoard().canReachCell(destRow, destCol)) return false;

        ActionList actions = ActionFactory.generateMoveWithinContainer(model, pawn, destRow, destCol);
        actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
        afterPlay(destRow, destCol);

        return true;
    }

    /**
     * Checks for captures
     */
    public void afterPlay(int destRow, int destCol) {
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        TablutStageModel gameStage = (TablutStageModel) model.getGameStage();
        TablutBoard board = gameStage.getBoard();
        Pawn pawn = (Pawn) board.getElement(destRow, destCol);

        for (int[] vect : directions) {

            // first check if the next cell is an ennemy pawn
            int[] position = {destRow + vect[0], destCol + vect[1]};

            if (board.outOfBounds(new Coordinate(position[0], position[1])))
                continue;

            if (board.isEmptyAt(position[0], position[1]))
                continue;

            Pawn ennemyPawn = (Pawn) board.getElement(position[0], position[1]);
            if (ennemyPawn.getTeam() == pawn.getTeam())
                continue;

            // checks the further cell if it is a ally pawn (capture)
            position[0] += vect[0];
            position[1] += vect[1];

            if (position[0] < 0 || position[0] > 8) continue;
            if (position[1] < 0 || position[1] > 8) continue;

            if (board.isEmptyAt(position[0], position[1]) && !gameStage.isFortressAt(position[0], position[1]))
                continue;

            Pawn otherPawn = (Pawn) board.getElement(position[0], position[1]);

            if ((gameStage.isFortressAt(position[0], position[1]) || otherPawn.getTeam() == pawn.getTeam()) && ennemyPawn.shouldDie()) {
                ActionList actions = ActionFactory.generateRemoveFromContainer(model, ennemyPawn);
                ActionPlayer play = new ActionPlayer(model, this, actions);
                play.start();
                ennemyPawn.setVisible(false);
            }
        }
    }

    public boolean displayAndApplyMenu() {
        boolean done;
        OptionMenu optionMenu = new OptionMenu();

        TablutStageModel stageModel = (TablutStageModel) model.getGameStage();
        MenuElement menuElement = stageModel.getMenuElement();

        if (menuElement != null) {
            menuElement.clearEntries();
            menuElement.setVisible(true);

            menuElement.addEntry(new MenuEntry(0, "King move mode", new ArrayList<>(), menuElement.getMenuEntries().size() + 1));
            menuElement.addEntry(new MenuEntry(0, "Win zone", new ArrayList<>(), menuElement.getMenuEntries().size() + 1));

            menuElement.getMenuEntries().get(0).addSubmenuEntry(new MenuEntry(1, "Without distance limitations", new ArrayList<>(), menuElement.getMenuEntries().get(0).getSubmenu().size() + 1));
            menuElement.getMenuEntries().get(0).addSubmenuEntry(new MenuEntry(1, "Single square distance limitation", new ArrayList<>(), menuElement.getMenuEntries().get(0).getSubmenu().size() + 1));

            menuElement.getMenuEntries().get(1).addSubmenuEntry(new MenuEntry(1, "Borders", new ArrayList<>(), menuElement.getMenuEntries().get(1).getSubmenu().size() + 1));
            menuElement.getMenuEntries().get(1).addSubmenuEntry(new MenuEntry(1, "Corners", new ArrayList<>(), menuElement.getMenuEntries().get(1).getSubmenu().size() + 1));

            menuElement.addPrompt("Choose an option (ex: 1A) or type 'save' to exit");
            menuElement.setMenuString();
            update();
        }

        done = optionMenu.readMenu(stageModel);

        if (menuElement != null) {
            menuElement.setVisible(false);

            if (!done) {
                stageModel.getBoard().setVisible(false);
                menuElement.setLocation(0, 0);
            } else {
                stageModel.getBoard().setVisible(true);
                int boardY = (int) stageModel.getBoard().getY();
                int menuY = boardY + (stageModel.getBoard().getNbRows() * 2) + 3;
                menuElement.setLocation(0, menuY);
            }
        }


        return done;
    }

    public void openMenu() {
        boolean done;
        do {
            done = displayAndApplyMenu();
        } while (!done);
    }
}
