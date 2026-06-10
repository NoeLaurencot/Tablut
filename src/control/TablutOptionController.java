package control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.InvalidIntegerOptionException;
import model.TablutOptionModel;
import view.TablutOptionView;



public class TablutOptionController implements EventHandler<ActionEvent> {
    private TablutOptionModel tablutOptionModel;
    private TablutOptionView tablutOptionView;


    public TablutOptionController(TablutOptionModel tablutOptionModel,TablutOptionView tablutOptionView) {
        this.tablutOptionModel = tablutOptionModel;
        this.tablutOptionView = tablutOptionView;
    }


    @Override
    public void handle(ActionEvent event) {
        try{
            tablutOptionModel.setGamemode(getGamemode(tablutOptionView.getSwedishPlayer().getValue(),tablutOptionView.getMuscovitesPlayer().getValue()));
            tablutOptionModel.setKingMode(tablutOptionView.getSelectedKingMoveRadio());
            tablutOptionModel.setWinMode(tablutOptionView.getSelectedWinModeRadio());
            tablutOptionModel.setEvenColor(tablutOptionView.getEvenColorPicker().getValue());
            tablutOptionModel.setOddColor(tablutOptionView.getOddColorPicker().getValue());
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
