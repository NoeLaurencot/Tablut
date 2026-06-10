package view;

import control.TablutOptionController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.TablutOptionModel;

import java.util.ArrayList;
import java.util.List;

public class TablutOptionView {
    private Label title;

    FlowPane playersFlowPane;
    VBox swedishVBox;
    VBox muscoviteVBox;

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

        validButton = new Button("Apply");
        doneButton = new Button("Done");
    }

    public Node getOptionMenu() {

        this.initFromModelValue();

        validButton.setDisable(false);

        VBox main = new VBox();

        /// Title

        title = new Label("Tablut settings");

        styleTitle();

        Separator separator = new Separator();

        VBox titleVBox = new VBox();

        titleVBox.getChildren().addAll(title, separator);

        /// Players

        Label catPlayer = new Label("Players");

        playersFlowPane = new FlowPane(15, 5);

        swedishVBox = new VBox(5);
        Label labelSwedish = new Label("Swedish");
        Separator swedishSep = new Separator();

        swedishVBox.getChildren().addAll(labelSwedish, swedishSep, swedishPlayer, swedishAI);

        Label labelVS = new Label("VS");

        muscoviteVBox = new VBox(5);
        Label labelMuscovites = new Label("Muscovites");
        Separator muscovitesSep = new Separator();

        muscoviteVBox.getChildren().addAll(labelMuscovites, muscovitesSep, muscovitesPlayer, muscovitesAI);

        playersFlowPane.getChildren().addAll(swedishVBox, labelVS, muscoviteVBox);

        /// Rules ///

        Label catRules = new Label("Rules");

        HBox winModeHbox = new HBox();
        winModeHbox.setSpacing(10);
        Label winModeLabel = new Label("Win condition");
        winModeHbox.getChildren().add(winModeLabel);
        winModeRadios.forEach(radioButton -> winModeHbox.getChildren().add(radioButton));

        HBox kingMoveHbox = new HBox();
        kingMoveHbox.setSpacing(10);
        Label kingMoveLabel = new Label("King movement :");
        kingMoveHbox.getChildren().add(kingMoveLabel);
        kingMoveRadios.forEach(radioButton -> kingMoveHbox.getChildren().add(radioButton));

        /// Color pickers ///

        Label catColors = new Label("Theme");

        Label labelOddColor = new Label("Odd Color");
        Label labelEvenColor = new Label("Even Color");
        GridPane colorGridPane = new GridPane(5, 5);
        colorGridPane.add(labelEvenColor, 0, 0);
        colorGridPane.add(labelOddColor, 0, 1);
        colorGridPane.add(evenColorPicker, 1, 0);
        colorGridPane.add(oddColorPicker, 1, 1);

        /// Buttons ///

        FlowPane buttonFlowPane = new FlowPane();
        buttonFlowPane.setAlignment(Pos.BOTTOM_RIGHT);
        buttonFlowPane.getChildren().addAll(validButton, doneButton);
        buttonFlowPane.setHgap(5);

        ///////////////

        main.getChildren().addAll(titleVBox, catPlayer, playersFlowPane, catRules, kingMoveHbox, winModeHbox, catColors, colorGridPane, buttonFlowPane);

        main.setPrefWidth(600);

        main.setSpacing(20);
        main.setPadding(new Insets(10));

        styleElements();
        styleCategories(catPlayer, catRules, catColors);

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

    private void styleElements() {
        styleTitle();
        styleButtons();
        styleCards();
    }

    private void styleTitle() {
        title.setStyle("""
                -fx-font-size: 50;
                -fx-font-weight: bold;
                """);
    }

    private void styleButtons() {
        Button[] btns = {validButton, doneButton};

        for (Button btn : btns) {
            btn.setStyle("""
                    -fx-padding: 5 20 5 20;
                    """);
        }
    }

    private void styleCards() {
        VBox[] cards = {swedishVBox, muscoviteVBox};

        for (VBox card : cards) {
            card.setStyle("""
                    -fx-padding: 10;
                    -fx-border-radius: 5;
                    -fx-border-width: 1;
                    -fx-border-color: #b8b8b8;
                    """);
            for (Node elmnt : card.getChildren()) {
                if (elmnt instanceof ComboBox<?>) {
                    ((ComboBox) elmnt).setPrefWidth(150);
                }
            }
        }
    }

    private void styleCategories(Label... labels) {
        for (Label label: labels) {
            label.setStyle("-fx-font-weight: bold;");
        }
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
