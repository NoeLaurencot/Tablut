package control;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;
import model.AIMode;
import model.InvalidOptionException;
import model.TablutOptionModel;
import model.Theme;
import view.TablutOptionView;
import view.TablutView;

import java.util.ArrayList;


public class TablutOptionController implements EventHandler<ActionEvent> {
    private TablutOptionView tablutOptionView;
    private TablutView tablutView;


    public TablutOptionController(TablutOptionView tablutOptionView, TablutView tablutView) {
        this.tablutOptionView = tablutOptionView;
        this.tablutView = tablutView;
    }


    @Override
    public void handle(ActionEvent event) {
        Object src = event.getSource();
        if (src.equals(this.tablutOptionView.getDoneButton())) {
            applyOptions();
            this.tablutView.resetView();
        } else if (src.equals(this.tablutOptionView.getValidButton())) {
            applyOptions();
            this.tablutOptionView.getValidButton().setDisable(true);
        } else if (src.equals(this.tablutOptionView.getSwedishPlayer())
                || src.equals(this.tablutOptionView.getMuscovitesPlayer())
                || src.equals(this.tablutOptionView.getThemeComboBox())) {
            handleComboBox(event);
        } else if (src.equals(this.tablutOptionView.getAdvancedColorsCheckBox())) {
            handleAdvancedCheckBox();
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
        } else if (event.getSource().equals(tablutOptionView.getThemeComboBox())) {
            if (tablutOptionView.getThemeComboBox().getValue().equals("Dark")) {
                tablutOptionView.getMainColorPicker().setValue(Color.rgb(27, 32, 39));
                tablutOptionView.getSecondColorPicker().setValue(Color.rgb(65, 70, 80));
                tablutOptionView.getFontColorPicker().setValue(Color.WHITE);
            } else {
                tablutOptionView.getMainColorPicker().setValue(Color.rgb(170, 170, 170));
                tablutOptionView.getSecondColorPicker().setValue(Color.rgb(90, 90, 90));
                tablutOptionView.getFontColorPicker().setValue(Color.BLACK);
            }
        }
    }

    private void handleAdvancedCheckBox() {
        boolean isAdvanced = tablutOptionView.getAdvancedColorsCheckBox().isSelected();

        tablutOptionView.getAdvancedColorGridPane().setDisable(!isAdvanced);
    }

    private void applyOptions() {
        try {
            TablutOptionModel.setAiMode(getAiMode(tablutOptionView.getSwedishAI(), tablutOptionView.getMuscovitesAI()));
            TablutOptionModel.setGamemode(getGamemode(tablutOptionView.getSwedishPlayer(), tablutOptionView.getMuscovitesPlayer()));
            TablutOptionModel.setKingMode(tablutOptionView.getSelectedKingMoveRadio());
            TablutOptionModel.setWinMode(tablutOptionView.getSelectedWinModeRadio());
            TablutOptionModel.setMoveAnywhere(tablutOptionView.getMoveCheckBox().isSelected());
            TablutOptionModel.setCaptureDisabled(tablutOptionView.getCaptureCheckBox().isSelected());
            TablutOptionModel.setMoveAnyPawn(tablutOptionView.getAnyPlayCheckBox().isSelected());
            TablutOptionModel.setBuilderMode(tablutOptionView.getBuilderMode().isSelected());
            TablutOptionModel.setColorTheme(tablutOptionView.getThemeComboBox().getValue().equals("Dark") ? Theme.DARK : Theme.LIGHT);
            TablutOptionModel.setAdvancedColors(tablutOptionView.getAdvancedColorsCheckBox().isSelected());
            TablutOptionModel.setMainColor(tablutOptionView.getMainColorPicker().getValue());
            TablutOptionModel.setSecondColor(tablutOptionView.getSecondColorPicker().getValue());
            TablutOptionModel.setFontColor(tablutOptionView.getFontColorPicker().getValue());
            TablutOptionModel.setEvenColor(tablutOptionView.getEvenColorPicker().getValue());
            TablutOptionModel.setOddColor(tablutOptionView.getOddColorPicker().getValue());
            TablutOptionModel.setMinmaxDepth(tablutOptionView.getMinmaxDepth());
        } catch (InvalidOptionException e) {
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
