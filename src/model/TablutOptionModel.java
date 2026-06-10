package model;

import control.TablutOptionController;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class TablutOptionModel {

    // mode 0 : Human vs Human
    // mode 1 : Human vs Computer
    // mode 2 : Computer vs Human
    // mode 3 : Computer vs Computer
    private static int gamemode = 1;
    private static WinMode winMode = WinMode.BORDERS;
    private static KingMoveMode kingMode = KingMoveMode.LIMITLESS;

    private static ArrayList<AIMode> aiMode = new ArrayList<>(List.of(AIMode.SIMPLE_EVAL));

    private static Color oddColor = Color.rgb(194,172,151);
    private static Color evenColor = Color.BEIGE;



    public static int getGamemode() {
        return TablutOptionModel.gamemode;
    }

    public static void setGamemode(int gamemode) throws InvalidIntegerOptionException  {
        if (gamemode < 0 || gamemode > 3) throw new InvalidIntegerOptionException("Gamemode",gamemode);
        TablutOptionModel.gamemode = gamemode;
    }

    public static ArrayList<AIMode> getAiMode() {
        return aiMode;
    }

    public static Color getEvenColor() {
        return evenColor;
    }

    public static Color getOddColor() {
        return oddColor;
    }

    public static KingMoveMode getKingMode() {
        return kingMode;
    }

    public static WinMode getWinMode() {
        return winMode;
    }

    public static void setAiMode(ArrayList<AIMode> aiMode) {
        TablutOptionModel.aiMode = aiMode;
    }

    public static void setEvenColor(Color evenColor) {
        TablutOptionModel.evenColor = evenColor;
    }

    public static void setOddColor(Color oddColor) {
        TablutOptionModel.oddColor = oddColor;
    }

    public static void setKingMode(KingMoveMode kingMode) throws InvalidIntegerOptionException {
        TablutOptionModel.kingMode = kingMode;
    }

    public static void setWinMode(WinMode winMode) throws InvalidIntegerOptionException {
        TablutOptionModel.winMode = winMode;
    }
}