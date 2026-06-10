package control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.InvalidIntegerOptionException;
import model.TablutOptionModel;
import view.TablutOptionView;



public class TablutOptionController implements EventHandler<ActionEvent> {
    private TablutOptionView tablutOptionView;


    public TablutOptionController(TablutOptionView tablutOptionView) {
        this.tablutOptionView = tablutOptionView;
    }


    @Override
    public void handle(ActionEvent event) {
        try{
            TablutOptionModel.setGamemode(getGamemode(tablutOptionView.getSwedishPlayer().getValue(),tablutOptionView.getMuscovitesPlayer().getValue()));
            TablutOptionModel.setKingMode(tablutOptionView.getSelectedKingMoveRadio());
            TablutOptionModel.setWinMode(tablutOptionView.getSelectedWinModeRadio());
            TablutOptionModel.setEvenColor(tablutOptionView.getEvenColorPicker().getValue());
            TablutOptionModel.setOddColor(tablutOptionView.getOddColorPicker().getValue());
        } catch (InvalidIntegerOptionException e) {
            throw new RuntimeException(e);
        }

    }

    private int getGamemode(String swedish,String muscovites) {
        if (swedish.equals("Human") && swedish.equals(muscovites)) {
            return 0;
        } else if (swedish.equals("Computer") && !swedish.equals(muscovites)) {
            return 1;
        } else if ((swedish.equals("Human"))) {
            return 2;
        } else {
            return 3;
        }
    }



}
