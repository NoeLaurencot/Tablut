package view;

import boardifier.model.Model;
import boardifier.view.RootPane;
import boardifier.view.View;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.TablutOptionModel;
import model.TablutStageModel;

public class TablutView extends View {

    private MenuItem menuStart;
    private MenuItem menuIntro;
    private MenuItem menuOption;
    private MenuItem menuRules;
    private MenuItem menuQuit;
    private TablutOptionView tablutOptionView;
    private TablutRulesView tablutRulesView;


    public TablutView(Model model, Stage stage, RootPane rootPane) {
        super(model, stage, rootPane);
    }

    @Override
    protected void createMenuBar() {
        menuBar = new MenuBar();
        Menu menu1 = new Menu("Game");
        menuStart = new MenuItem("New game");
        menuRules = new MenuItem("Rules");
        menuOption = new MenuItem("Settings");
        menuIntro = new MenuItem("Main menu");
        menuQuit = new MenuItem("Quit");
        tablutOptionView = new TablutOptionView(this);
        tablutRulesView = new TablutRulesView(this);
        menu1.getItems().addAll(menuStart,menuRules,menuOption,menuIntro,menuQuit);
        menuBar.getMenus().add(menu1);
    }

    public MenuItem getMenuStart() {
        return menuStart;
    }

    public MenuItem getMenuIntro() {
        return menuIntro;
    }

    public MenuItem getMenuQuit() {
        return menuQuit;
    }

    public MenuItem getMenuOption() {return  menuOption;}

    public MenuItem getMenuRules() {return menuRules;}

    public void showOptionMenu() {
        rootPane.getChildren().clear();
        rootPane.getChildren().addAll(tablutOptionView.getOptionMenu());
    }
    public void showRulesMenu() {
        rootPane.getChildren().clear();
        rootPane.getChildren().addAll(tablutRulesView.getRulesMenu());

    }

    public TablutOptionView getTablutOptionView() {
        return tablutOptionView;
    }
}