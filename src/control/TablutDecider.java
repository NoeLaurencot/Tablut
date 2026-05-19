package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.TablutBoard;
import model.TablutStageModel;
import model.Pawn;

import java.awt.*;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class TablutDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    private List<TablutBoard> seenBoards;

    public TablutDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        TablutStageModel stage = (TablutStageModel)model.getGameStage();
        TablutBoard board = stage.getBoard();
        GameElement pawn = null;
        int rowDest = 0;
        int colDest = 0;



        ActionList actions = ActionFactory.generatePutInContainer( model, pawn, "tablutboard", rowDest, colDest);
        actions.setDoEndOfTurn(true);

        return actions;
    }

    public int evaluate(TablutBoard board) {
        if (board.areSwedishWinning()) return 1000;
        if (board.areMuscovitesWinning()) return -1000;
    
        return 0;
    }
}
