package model;

import javafx.scene.paint.Color;

public class TablutOptionModel {

    // mode 0 : Human vs Human
    // mode 1 : Human vs Computer
    // mode 2 : Computer vs Human
    // mode 3 : Computer vs Computer
    private static int gamemode = 1;
    // mode 0 edges
    // mode 1 corners
    private static int winMode = 0;
    // mode 0 long move
    // mode 1 short move
    private static int kingMode = 0;

    private static Color  oddColor = Color.rgb(194,172,151);
    private static Color evenColor = Color.BEIGE;



    public static int getGamemode() {
        return TablutOptionModel.gamemode;
    }

    public static void setGamemode(int gamemode) throws InvalidIntegerOptionException  {
        if (gamemode < 0 || gamemode > 3) throw new InvalidIntegerOptionException("Gamemode",gamemode);
        TablutOptionModel.gamemode = gamemode;
    }

    public static Color getEvenColor() {
        return evenColor;
    }

    public static Color getOddColor() {
        return oddColor;
    }

    public static int getKingMode() {
        return kingMode;
    }

    public static int getWinMode() {
        return winMode;
    }

    public static void setEvenColor(Color evenColor) {
        TablutOptionModel.evenColor = evenColor;
    }

    public static void setOddColor(Color oddColor) {
        TablutOptionModel.oddColor = oddColor;
    }

    public static void setKingMode(int kingMode) throws InvalidIntegerOptionException {
        if (kingMode < 0 || kingMode > 1) throw new InvalidIntegerOptionException("King Mode",kingMode);
        TablutOptionModel.kingMode = kingMode;
    }

    public static void setWinMode(int winMode) throws InvalidIntegerOptionException {
        if (winMode < 0 || winMode > 1) throw new InvalidIntegerOptionException("Win Mode",winMode);
        TablutOptionModel.winMode = winMode;
    }
}