package view;

import boardifier.view.RootPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TablutRootPane extends RootPane {

    public TablutRootPane() {
        super();
    }

    private Button btnNewGame;
    private Button btnSettings;
    private Button btnRules;
    private Button btnQuit;


    @Override
    public void createDefaultGroup() {

//        Rectangle frame = new Rectangle(600, 600, Color.LIGHTGREY);
//        Text text = new Text("Playing Tablut");
//        text.setFont(new Font(15));
//        text.setFill(Color.BLACK);
//        text.setX(10);
//        text.setY(50);
//        // put shapes in the group
//        group.getChildren().clear();
//        group.getChildren().addAll(frame, text);

        this.getStylesheets().add(getClass().getResource("/menu.css").toExternalForm());
        Rectangle frame = new Rectangle(800, 500, Color.LIGHTGREY);

        HBox root = new HBox();
        root.setPrefSize(800, 500);

        VBox vBoxLeft = new VBox();
        VBox vBoxRight = new VBox();
        root.getChildren().addAll(vBoxLeft, vBoxRight);

        Label text = new Label("Tablut");
        text.getStyleClass().add("title-label");
        text.setFont(new Font(50));

        vBoxLeft.getChildren().add(text);
        vBoxLeft.setAlignment(Pos.TOP_LEFT);

        if (btnNewGame == null) {
            btnNewGame = new Button("New Game");
            btnNewGame.getStyleClass().add("menu-button");
            btnNewGame.setMaxWidth(Double.MAX_VALUE);
        }
        if (btnSettings == null) {
            btnSettings = new Button("Settings");
            btnSettings.getStyleClass().add("menu-button");
            btnSettings.setMaxWidth(Double.MAX_VALUE);
        }
        if (btnRules == null) {
            btnRules = new Button("Show Rules");
            btnRules.getStyleClass().add("menu-button");
            btnRules.setMaxWidth(Double.MAX_VALUE);
        }
        if (btnQuit == null) {
            btnQuit = new Button("Quit ?");
            btnQuit.getStyleClass().add("menu-button");
            btnQuit.setMaxWidth(Double.MAX_VALUE);
        }
        vBoxRight.getChildren().addAll(btnNewGame, btnSettings,btnRules, btnQuit);
        vBoxRight.setSpacing(25);
        vBoxRight.setPadding(new Insets(30));
        vBoxRight.setAlignment(Pos.CENTER);

        root.setSpacing(20);
        root.setPadding(new Insets(20));

        HBox.setHgrow(vBoxLeft, Priority.ALWAYS);
        HBox.setHgrow(vBoxRight, Priority.ALWAYS);

        vBoxLeft.setFillWidth(true);
        vBoxLeft.setSpacing(20);

        group.getChildren().clear();
        group.getChildren().addAll(frame, root);
    }

    public Button getBtnNewGame() {
        return btnNewGame;
    }

    public Button getBtnQuit() {
        return btnQuit;
    }

    public Button getBtnSettings() {
        return btnSettings;
    }

    public Button getBtnRules() {
        return btnRules;
    }
}
