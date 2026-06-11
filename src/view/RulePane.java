package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class RulePane extends BorderPane {

    private Button btnBack;
    public RulePane(String titleStr, String rulesStr) {
        this.setStyle(
                "-fx-background-color: #ffffff;" +
                        "-fx-background-radius: 12px;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 1px;"
        );
        this.setPadding(new Insets(25));

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_LEFT);
        vBox.setSpacing(18);

        Label title = new Label(titleStr);
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        title.setStyle(
                "-fx-text-fill: #1a1a1a;" +
                        "-fx-border-color: #1a1a1a;" +
                        "-fx-border-width: 0 0 3px 0;" +
                        "-fx-padding: 0 0 8px 0;"
        );

        Label rules = new Label(rulesStr);
        rules.setStyle("-fx-text-fill: #4a4a4a;");
        rules.setWrapText(true);
        rules.setLineSpacing(6.0);

        vBox.getChildren().addAll(title, rules);
        btnBack = new Button("Back");

        HBox buttonContainer = new HBox(btnBack);
        buttonContainer.setAlignment(Pos.BOTTOM_RIGHT);

        this.setCenter(vBox);
        this.setBottom(buttonContainer);
    }

    public Button getBtnBack() {
        return btnBack;
    }
}