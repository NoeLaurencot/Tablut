package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.GameElement;
import boardifier.model.ContainerElement;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import model.Pawn;
import model.TablutStageModel;
import model.TablutBoard;
import model.Team;
import view.TablutStageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

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
     */
    public void stageLoop() {
        consoleIn = new BufferedReader(new InputStreamReader(System.in));

        update();
        while (!model.isEndStage()) {
            playTurn();
            endOfTurn();
            update();
        }
        endGame();
    }

    private void playTurn() {
        // get the new player
        Player p = model.getCurrentPlayer();
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            TablutDecider decider = new TablutDecider(model, this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        } else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName() + " > ");
                try {
                    String line = consoleIn.readLine();
                    if (line.length() == 4) {
                        ok = analyseAndPlay(line);
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
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (stageModel.getBoard().isEmptyAt(i, j)) {
                    System.out.print(1);
                } else {
                System.out.print(0);
                }
            }
            System.out.println();
        }
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
    private void afterPlay(int destRow, int destCol) {
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        TablutStageModel gameStage = (TablutStageModel) model.getGameStage();
        TablutBoard board = gameStage.getBoard();
        Pawn pawn = (Pawn) board.getElement(destRow, destCol);

        for (int[] vect : directions) {

            // first check if the next cell is an ennemy pawn
            int[] position = {destRow + vect[0], destCol + vect[1]};

            if (position[0] < 0 || position[0] > 8) continue;
            if (position[1] < 0 || position[1] > 8) continue;

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
}
