package view;

import control.TablutOptionController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.TablutOptionModel;

import java.util.ArrayList;
import java.util.List;

public class TablutOptionView {
    private List<RadioButton> kingMoveRadios;
    private ToggleGroup kingMoveGroup;
    private List<RadioButton> winModeRadios;
    private ToggleGroup winModeGroup;
    private ComboBox<String> swedishPlayer;
    private ComboBox<String> muscovitesPlayer;
    private ComboBox<String> swedishAI;
    private ComboBox<String> muscovitesAI;
    private ColorPicker evenColorPicker;
    private ColorPicker oddColorPicker;
    private Button validButton;
    private Button doneButton;


    public TablutOptionView() {
        this.initWidget();
        this.initController();
    }

    private void initController() {
        validButton.setOnAction(new TablutOptionController(this));
        doneButton.setOnAction(new TablutOptionController(this));
        swedishPlayer.setOnAction(new TablutOptionController(this));
        muscovitesPlayer.setOnAction(new TablutOptionController(this));

        kingMoveGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            reEnableValidButton();
        });
        winModeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            reEnableValidButton();
        });
        swedishPlayer.valueProperty().addListener((observable, oldValue, newValue) -> {
            reEnableValidButton();
        });
        muscovitesPlayer.valueProperty().addListener((observable, oldValue, newValue) -> {
            reEnableValidButton();
        });
        swedishAI.valueProperty().addListener((observable, oldValue, newValue) -> {
            reEnableValidButton();
        });
        muscovitesAI.valueProperty().addListener((observable, oldValue, newValue) -> {
            reEnableValidButton();
        });
        evenColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            reEnableValidButton();
        });
        oddColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            reEnableValidButton();
        });
    }

    private void initWidget() {
        validButton = new Button("Apply");
        doneButton = new Button("Done");

        kingMoveRadios = new ArrayList<>();
        kingMoveRadios.add(new RadioButton("Long move king"));
        kingMoveRadios.add(new RadioButton("Short move king"));
        kingMoveGroup = new ToggleGroup();
        kingMoveRadios.forEach(radioButton -> radioButton.setToggleGroup(kingMoveGroup));

        winModeRadios = new ArrayList<>();
        winModeRadios.add(new RadioButton("Edges"));
        winModeRadios.add(new RadioButton("Corners"));
        winModeGroup = new ToggleGroup();
        winModeRadios.forEach(radioButton -> radioButton.setToggleGroup(winModeGroup));

        swedishPlayer = new ComboBox<>(FXCollections.observableArrayList("Human", "Computer"));
        muscovitesPlayer = new ComboBox<>(FXCollections.observableArrayList("Human", "Computer"));
        swedishAI = new ComboBox<>(FXCollections.observableArrayList("Simple", "Minmax"));
        muscovitesAI = new ComboBox<>(FXCollections.observableArrayList("Simple", "Minmax"));
        evenColorPicker = new ColorPicker();
        oddColorPicker = new ColorPicker();
    }

    public Node getOptionMenu() {

        this.initFromModelValue();

        VBox main = new VBox();
        VBox vBox1 = new VBox();

        GridPane playersGridPane = new GridPane(5, 5);
        Label labelVS = new Label("VS");
        playersGridPane.add(swedishPlayer, 0, 1);
        playersGridPane.add(labelVS, 1, 1);
        playersGridPane.add(muscovitesPlayer, 2, 1);
        Label labelSwedish = new Label("Swedish");
        Label labelMuscovites = new Label("Muscovites");
        playersGridPane.add(labelSwedish, 0, 0);
        playersGridPane.add(labelMuscovites, 2, 0);

        playersGridPane.add(swedishAI, 0, 2);
        playersGridPane.add(muscovitesAI, 2, 2);

        Label labelOddColor = new Label("Odd Color");
        Label labelEvenColor = new Label("Even Color");
        GridPane gridPane2 = new GridPane(5, 5);
        gridPane2.add(labelEvenColor, 0, 0);
        gridPane2.add(labelOddColor, 0, 1);
        gridPane2.add(evenColorPicker, 1, 0);
        gridPane2.add(oddColorPicker, 1, 1);

        HBox kingMoveHbox = new HBox();
        kingMoveHbox.setSpacing(10);
        Label kingMoveLabel = new Label("King movement :");
        kingMoveHbox.getChildren().add(kingMoveLabel);
        kingMoveRadios.forEach(radioButton -> kingMoveHbox.getChildren().add(radioButton));


        HBox winModeHbox = new HBox();
        winModeHbox.setSpacing(10);
        Label winModeLabel = new Label("Win area :");
        winModeHbox.getChildren().add(winModeLabel);
        winModeRadios.forEach(radioButton -> winModeHbox.getChildren().add(radioButton));

        FlowPane buttonFlowPane = new FlowPane();
        buttonFlowPane.getChildren().addAll(validButton, doneButton);
        buttonFlowPane.setHgap(5);

        vBox1.getChildren().add(playersGridPane);

        main.getChildren().addAll(vBox1, gridPane2, kingMoveHbox, winModeHbox, buttonFlowPane);

        main.setSpacing(20);
        main.setPadding(new Insets(10));

        return main;
    }

    private void initFromModelValue() {
        switch (TablutOptionModel.getGamemode()) {
            case 0 -> {
                swedishPlayer.setValue("Human");
                muscovitesPlayer.setValue("Human");
            }
            case 1 -> {
                swedishPlayer.setValue("Human");
                muscovitesPlayer.setValue("Computer");
            }
            case 2 -> {
                swedishPlayer.setValue("Computer");
                muscovitesPlayer.setValue("Human");
            }
            case 3 -> {
                swedishPlayer.setValue("Computer");
                muscovitesPlayer.setValue("Computer");
            }
        }

        swedishAI.setDisable(swedishPlayer.getValue().equals("Human"));
        muscovitesAI.setDisable(muscovitesPlayer.getValue().equals("Human"));

        switch (TablutOptionModel.getGamemode()) {
            case 0 -> {
                swedishAI.setValue("Simple");
                swedishAI.setValue("Simple");
            }
            case 1 -> {
                swedishAI.setValue("Simple");
                muscovitesAI.setValue(TablutOptionModel.getAiMode().getFirst().toString());
            }
            case 2 -> {
                swedishAI.setValue(TablutOptionModel.getAiMode().getFirst().toString());
                muscovitesAI.setValue("Simple");
            }
            case 3 -> {
                swedishAI.setValue(TablutOptionModel.getAiMode().getFirst().toString());
                muscovitesAI.setValue(TablutOptionModel.getAiMode().getLast().toString());
            }
        }

        winModeRadios.get(TablutOptionModel.getWinMode()).setSelected(true);
        kingMoveRadios.get(TablutOptionModel.getKingMode()).setSelected(true);
        oddColorPicker.setValue(TablutOptionModel.getOddColor());
        evenColorPicker.setValue(TablutOptionModel.getEvenColor());
    }

    public Button getValidButton() {
        return validButton;
    }

    public Button getDoneButton() {
        return doneButton;
    }

    public ColorPicker getEvenColorPicker() {
        return evenColorPicker;
    }

    public ColorPicker getOddColorPicker() {
        return oddColorPicker;
    }

    public ComboBox<String> getMuscovitesAI() {
        return muscovitesAI;
    }

    public ComboBox<String> getSwedishAI() {
        return swedishAI;
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

    public void reEnableValidButton() {
        if (validButton.isDisabled()) {
            validButton.setDisable(false);
        }
    }
}
