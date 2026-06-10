package control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import model.AIMode;
import model.InvalidIntegerOptionException;
import model.TablutOptionModel;
import view.TablutOptionView;

import java.util.ArrayList;


public class TablutOptionController implements EventHandler<ActionEvent> {
    private TablutOptionView tablutOptionView;


    public TablutOptionController(TablutOptionView tablutOptionView) {
        this.tablutOptionView = tablutOptionView;
    }


    @Override
    public void handle(ActionEvent event) {
        if (event.getSource().equals(this.tablutOptionView.getDoneButton())) {
            applyOptions();
        } else if (event.getSource().equals(this.tablutOptionView.getValidButton())) {
            applyOptions();
            this.tablutOptionView.getValidButton().setDisable(true);
        } else if (event.getSource().equals(this.tablutOptionView.getSwedishPlayer()) || event.getSource().equals(this.tablutOptionView.getMuscovitesPlayer())) {
            handleComboBox(event);
        }
    }

    private void handleComboBox(ActionEvent event) {
        if (event.getSource().equals(this.tablutOptionView.getSwedishPlayer())) {
            if (this.tablutOptionView.getSwedishPlayer().getValue().equals("Human")) {
                this.tablutOptionView.getSwedishAI().setDisable(true);
            } else {
                this.tablutOptionView.getSwedishAI().setDisable(false);
            }
        } else if (event.getSource().equals(this.tablutOptionView.getMuscovitesPlayer())) {
            if (this.tablutOptionView.getMuscovitesPlayer().getValue().equals("Human")) {
                this.tablutOptionView.getMuscovitesAI().setDisable(true);
            } else {
                this.tablutOptionView.getMuscovitesAI().setDisable(false);
            }
        }
    }

    private void applyOptions() {
        try {
            TablutOptionModel.setAiMode(getAiMode(tablutOptionView.getSwedishAI(), tablutOptionView.getMuscovitesAI()));
            TablutOptionModel.setGamemode(getGamemode(tablutOptionView.getSwedishPlayer(), tablutOptionView.getMuscovitesPlayer()));
            TablutOptionModel.setKingMode(tablutOptionView.getSelectedKingMoveRadio());
            TablutOptionModel.setWinMode(tablutOptionView.getSelectedWinModeRadio());
            TablutOptionModel.setEvenColor(tablutOptionView.getEvenColorPicker().getValue());
            TablutOptionModel.setOddColor(tablutOptionView.getOddColorPicker().getValue());
        } catch (InvalidIntegerOptionException e) {
            throw new RuntimeException(e);
        }
    }

    private int getGamemode(ComboBox<String> swedish, ComboBox<String> muscovites) {
        if (swedish.getValue().equals("Human") && muscovites.getValue().equals("Human")) {
            return 0;
        } else if (swedish.getValue().equals("Human") && muscovites.getValue().equals("Computer")) {
            return 1;
        } else if (swedish.getValue().equals("Computer") && muscovites.getValue().equals("Human")) {
            return 2;
        } else {
            return 3;
        }
    }

    private ArrayList<AIMode> getAiMode(ComboBox<String> swedishAI, ComboBox<String> muscovitesAI) {
        ArrayList<AIMode> aiModes = new ArrayList<>();

        if (swedishAI.getValue().equals("Simple") && !swedishAI.isDisabled()) {
            aiModes.add(AIMode.SIMPLE_EVAL);
        } else if (swedishAI.getValue().equals("Minmax") && !swedishAI.isDisabled()) {
            aiModes.add(AIMode.MINMAX);
        }

        if (muscovitesAI.getValue().equals("Simple") && !muscovitesAI.isDisabled()) {
            aiModes.add(AIMode.SIMPLE_EVAL);
        } else if (muscovitesAI.getValue().equals("Minmax") && !muscovitesAI.isDisabled()) {
            aiModes.add(AIMode.MINMAX);
        }

        return aiModes;
    }


}
