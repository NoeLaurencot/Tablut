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

    public TablutDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of TablutStageModel
        TablutStageModel stage = (TablutStageModel)model.getGameStage();
        TablutBoard board = stage.getBoard(); // get the board
        GameElement pawn = null; // the pawn that is moved
        int rowDest = 0; // the dest. row in board
        int colDest = 0; // the dest. col in board

        ActionList actions = ActionFactory.generatePutInContainer( model, pawn, "tablutboard", rowDest, colDest);
        actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.

        return actions;
    }
}
