package view;

import boardifier.view.RootPane;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import model.TablutOptionModel;

import java.util.ArrayList;
import java.util.List;

public class TablutOptionView {
    private TablutOptionModel tablutOptionModel;

    private List<RadioButton> kingMoveRadios;
    private List<RadioButton> winModeRadios;
    private ComboBox<String> swedishPlayer;
    private ComboBox<String> muscovitesPlayer;
    private ColorPicker evenColorPicker;
    private ColorPicker oddColorPicker;
    private Button validButton;


    public TablutOptionView(TablutOptionModel tablutOptionModel) {
        this.tablutOptionModel = tablutOptionModel;
        this.initWidget();
        this.initController();
    }

    private void initController() {
    }

    private void initWidget() {
        validButton = new Button("Apply option");

        kingMoveRadios = new ArrayList<>();
        kingMoveRadios.add(new RadioButton("Long move king"));
        kingMoveRadios.add(new RadioButton("Short move king"));
        ToggleGroup kingMoveGroup = new ToggleGroup();
        kingMoveRadios.forEach(radioButton -> radioButton.setToggleGroup(kingMoveGroup));

        winModeRadios = new ArrayList<>();
        winModeRadios.add(new RadioButton("Edges"));
        winModeRadios.add(new RadioButton("Corners"));
        ToggleGroup winModeGroup = new ToggleGroup();
        winModeRadios.forEach(radioButton -> radioButton.setToggleGroup(winModeGroup));

        swedishPlayer = new ComboBox<>(FXCollections.observableArrayList(new String[]{"Human", "Computer"}));
        muscovitesPlayer = new ComboBox<>(FXCollections.observableArrayList(new String[]{"Human", "Computer"}));
        evenColorPicker = new ColorPicker();
        oddColorPicker = new ColorPicker();
    }

    public Node getOptionMenu() {
        FlowPane flowPane = new FlowPane();
//        flowPane.getChildren().add();
        return flowPane;
    }
}
