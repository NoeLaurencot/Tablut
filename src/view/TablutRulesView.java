package view;

import control.TablutOptionController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.KingMoveMode;
import model.TablutOptionModel;
import model.WinMode;

import java.util.ArrayList;
import java.util.List;

public class TablutRulesView {


    static String[][] RULES_SET = {
            {
                    "Presentation",
                    "Tablut is a strategy game for 2 players on a 9x9 checkerboard (81 squares). It pits 16 sword pawns (Muscovites) against 8 shield pawns and 1 King pawn (Swedish)."
            },
            {
                    "Objective - King",
                    "The player controlling the Swedish pawns wins if the King manages to escape by reaching any square on the periphery (the edge of the board)."
            },
            {
                    "Objective - Muscovites",
                    "The player controlling the Muscovite pawns wins if they manage to capture the King by surrounding him."
            },
            {
                    "Standard Movement",
                    "The first player is the Swedish player. On their turn, a player moves a pawn any number of squares, horizontally or vertically (like the rook in chess). The traversed squares and the destination square must be free.\n" +
                            "Clarification: The central square (E5, Throne) is occupied by the King at the start of the game. It is strictly forbidden to access or pass through for all other pawns (Swedish soldiers and Muscovites)."
            },
            {
                    "Capturing a Pawn",
                    "To capture an enemy pawn (Swedish or Muscovite), you must surround it by occupying two opposite adjacent squares, either horizontally or vertically. The captured pawn is removed from the game.\n" +
                            "Clarification: If a pawn voluntarily moves between two enemy pawns, it is not captured. The capture is only active for the player who has just played their turn.\n" +
                            "King: The King can actively participate in capturing an enemy pawn, exactly as if he were an ordinary soldier.\n" +
                            "Finally, it is possible to capture multiple enemy pawns in a single move if the played pawn simultaneously locks down multiple surroundings."
            },
            {
                    "Capturing the King",
                    "To capture the King in open ground, the Muscovites must occupy the 4 neighboring squares (up, down, left, right) to completely surround him.\n" +
                            "Exception: If the King is adjacent to the central square (E5, Throne), the Muscovites only need to occupy the 3 other neighboring squares to capture him, with the central square acting as the fourth obstacle."
            },
            {
                    "Raichi Announcement",
                    "When the King has a direct and free path to the periphery, the game will warn the opponent by declaring 'Raichi'. The Muscovites then have one turn to block the exit."
            },
            {
                    "Tuichi Announcement",
                    "When the King simultaneously has two free paths to the periphery, the game announces 'Tuichi'. Victory is immediate or assured on the next turn because the opponent cannot block two exits at the same time."
            },

            {
                    "Variant 1: Escape to the Corners",
                    "To win, the King must reach a corner square. These corners become fortresses.\n" +
                            "Fortresses : they can capture a pawn like two enemy pawn,and only the king go on them(to escape)."
            },
            {
                    "Variant 2: Snail King",
                    "The King can only move a maximum of 1 square per turn."
            }
    };
    private ListView<String> rulesOption;
    private RulePane rulesPane;
    private SplitPane main;



    public TablutRulesView(TablutView tablutView) {
        this.initWidget();
        this.initController(tablutView);
    }

    private void initController(TablutView tablutView) {
        rulesOption.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            for (String[] rule : RULES_SET) {
                if (rule[0].equals(newValue)) {
                    main.getItems().remove(rulesPane);
                    rulesPane = new RulePane(rule[0],rule[1]);
                    rulesPane.getBtnBack().setOnAction(event -> tablutView.resetView());
                    main.getItems().add(rulesPane);
                    break;
                }
            }
        });

    }

    private void initWidget() {
        rulesOption = new ListView<>();
        for (String[] rule : RULES_SET) {
            rulesOption.getItems().add(rule[0]);
        }
    }

    public Node getRulesMenu() {
        main = new SplitPane(rulesOption);
        main.setPrefSize(800,500);
        main.setDividerPosition(0,0.35);
        rulesOption.setMaxWidth(280);
        rulesOption.setMinWidth(280);
        rulesOption.setMaxHeight(500);
        rulesOption.setMinHeight(500);
        rulesOption.getSelectionModel().clearSelection();
        main.getStylesheets().add(getClass().getResource("/css/rules.css").toExternalForm());

        return main;
    }




}
