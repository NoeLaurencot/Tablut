package view;

import boardifier.view.RootPane;
import control.TablutOptionController;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.TablutOptionModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TablutOptionView {
    private List<RadioButton> kingMoveRadios;
    private List<RadioButton> winModeRadios;
    private ComboBox<String> swedishPlayer;
    private ComboBox<String> muscovitesPlayer;
    private ColorPicker evenColorPicker;
    private ColorPicker oddColorPicker;
    private Button validButton;


    public TablutOptionView() {
        this.initWidget();
        this.initController();
    }

    private void initController() {
        validButton.setOnAction(new TablutOptionController(this));
    }

    private void initWidget() {
        validButton = new Button("Apply option");

        kingMoveRadios = new ArrayList<>();
        kingMoveRadios.add(new RadioButton("Short move king"));
        kingMoveRadios.add(new RadioButton("Long move king"));
        ToggleGroup kingMoveGroup = new ToggleGroup();
        kingMoveRadios.forEach(radioButton -> radioButton.setToggleGroup(kingMoveGroup));

        winModeRadios = new ArrayList<>();
        winModeRadios.add(new RadioButton("Corners"));
        winModeRadios.add(new RadioButton("Edges"));
        ToggleGroup winModeGroup = new ToggleGroup();
        winModeRadios.forEach(radioButton -> radioButton.setToggleGroup(winModeGroup));

        swedishPlayer = new ComboBox<>(FXCollections.observableArrayList(new String[]{"Human", "Computer"}));
        muscovitesPlayer = new ComboBox<>(FXCollections.observableArrayList(new String[]{"Human", "Computer"}));
        evenColorPicker = new ColorPicker();
        oddColorPicker = new ColorPicker();
    }

    public Node getOptionMenu() {

        this.setModelValue();

        VBox main = new VBox();
        VBox vBox1 = new VBox();

        GridPane gridPane1 = new GridPane(5,5);
        Label labelVS = new Label("VS");
        gridPane1.add(swedishPlayer,0,1);
        gridPane1.add(labelVS,1,1);
        gridPane1.add(muscovitesPlayer,2,1);
        Label labelSwedish = new Label("Swedish");
        Label labelMuscovites = new Label("Muscovites");
        gridPane1.add(labelSwedish,0,0);
        gridPane1.add(labelMuscovites,2,0);

        Label labelOddColor = new Label("Odd Color");
        Label labelEvenColor = new Label("Even Color");
        GridPane gridPane2 = new GridPane(5,5);
        gridPane2.add(labelEvenColor,0,0);
        gridPane2.add(labelOddColor,0,1);
        gridPane2.add(evenColorPicker,1,0);
        gridPane2.add(oddColorPicker,1,1);

        HBox kingMoveHbox = new HBox();
        kingMoveHbox.setSpacing(10);
        Label kingMoveLabel = new Label("King GameMode :");
        kingMoveHbox.getChildren().add(kingMoveLabel);
        kingMoveRadios.forEach( radioButton -> kingMoveHbox.getChildren().add(radioButton));


        HBox winModeHbox = new HBox();
        winModeHbox.setSpacing(10);
        Label winModeLabel = new Label("Win GameMode :");
        winModeHbox.getChildren().add(winModeLabel);
        winModeRadios.forEach( radioButton -> winModeHbox.getChildren().add(radioButton));



        vBox1.getChildren().add(gridPane1);

        main.getChildren().addAll(vBox1,gridPane2,kingMoveHbox,winModeHbox,validButton);

        main.setSpacing(20);
        main.setPadding(new Insets(10));

        return main;
    }

    private void setModelValue() {
        winModeRadios.get(TablutOptionModel.getWinMode()).setSelected(true);
        kingMoveRadios.get(TablutOptionModel.getKingMode()).setSelected(true);
        switch (TablutOptionModel.getGamemode()){
            case 0:
                swedishPlayer.setValue("Human");
                muscovitesPlayer.setValue("Human");
                break;
            case 1:
                swedishPlayer.setValue("Computer");
                muscovitesPlayer.setValue("Human");
                break;
            case 2:
                swedishPlayer.setValue("Human");
                muscovitesPlayer.setValue("Computer");
                break;
            case 3:
                swedishPlayer.setValue("Computer");
                muscovitesPlayer.setValue("Computer");
                break;
        }
        oddColorPicker.setValue(TablutOptionModel.getOddColor());
        evenColorPicker.setValue(TablutOptionModel.getEvenColor());
    }

    public Button getValidButton() {
        return validButton;
    }

    public ColorPicker getEvenColorPicker() {
        return evenColorPicker;
    }

    public ColorPicker getOddColorPicker() {
        return oddColorPicker;
    }

    public ComboBox<String> getMuscovitesPlayer() {
        return muscovitesPlayer;
    }

    public ComboBox<String> getSwedishPlayer() {
        return swedishPlayer;
    }

    public int getSelectedKingMoveRadio() {
        for (int i = 0; i < kingMoveRadios.size(); i++) {
            if (kingMoveRadios.get(i).isSelected()) {
                return i;
            }
        }
        return 0;
    }

    public int getSelectedWinModeRadio() {
        for (int i = 0; i < winModeRadios.size(); i++) {
            if (winModeRadios.get(i).isSelected()) {
                return i;
            }
        }
        return 0;
    }

}
