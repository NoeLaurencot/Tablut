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
import model.TablutStageModel;
import view.TablutStageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        while(! model.isEndStage()) {
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
            TablutDecider decider = new TablutDecider(model,this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        }
        else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName()+ " > ");
                try {
                    String line = consoleIn.readLine();
                    if (line.length() == 4) {
                        ok = analyseAndPlay(line);
                    }
                    if (!ok) {
                        System.out.println("incorrect instruction. retry!");
                    }
                }
                catch(IOException e) {}
            }
        }
    }

    public void endOfTurn() {

        model.setNextPlayer();
        // get the new player to display its name
        Player p = model.getCurrentPlayer();
        TablutStageModel stageModel = (TablutStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());
    }
    private boolean analyseAndPlay(String line) {
        TablutStageModel gameStage = (TablutStageModel) model.getGameStage();
        // get the coords for the source pawn in the board
        int sourceCol = (int) (line.charAt(0) - 'A');
        int sourceRow = (int) (line.charAt(1) - '1');
        // check coords validity
        if ((sourceRow<0)||(sourceRow>9)) return false;
        if ((sourceCol<0)||(sourceCol>9)) return false;

        // get the ccords for the destination pawn in the board
        int destCol = (int) (line.charAt(3) - 'A');
        int destRow = (int) (line.charAt(4) - '1');
        // check coords validity
        if ((destRow<0)||(destRow>9)) return false;
        if ((destCol<0)||(destCol>9)) return false;

        // check if the pawn is still in its pot
    
        GameElement pawn = null;
        // compute valid cells for the chosen pawn
        gameStage.getBoard().setValidCells(pawnIndex+1);
        if (!gameStage.getBoard().canReachCell(row,col)) return false;

        ActionList actions = ActionFactory.generatePutInContainer(model, pawn, "Tablutboard", row, col);
        actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
        return true;
    }
}
