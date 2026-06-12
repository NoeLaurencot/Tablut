package model;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class TablutOptionModel {

    // mode 0 : Human vs Human
    // mode 1 : Human vs Computer
    // mode 2 : Computer vs Human
    // mode 3 : Computer vs Computer
    private static int gamemode = 0;
    private static int minmaxDepth = 4;
    private static WinMode winMode = WinMode.BORDERS;
    private static KingMoveMode kingMode = KingMoveMode.LIMITLESS;

    private static ArrayList<AIMode> aiMode = new ArrayList<>(List.of(AIMode.SIMPLE_EVAL));


    private static Theme colorTheme = Theme.DARK;
    private static boolean advancedColors = false;
    private static Color mainColor = Color.rgb(30, 40, 50);
    private static Color secondColor = Color.rgb(86,103,132);
    private static Color fontColor = Color.WHITE;
    private static Color oddColor = Color.rgb(194,172,151);
    private static Color evenColor = Color.BEIGE;

    // cheats
    private static boolean captureDisabled = false;
    private static boolean moveAnywhere = false;
    private static boolean moveAnyPawn = false;
    private static boolean builderMode = false;



    public static int getGamemode() {
        return TablutOptionModel.gamemode;
    }

    public static void setGamemode(int gamemode) throws InvalidOptionException {
        if (gamemode < 0 || gamemode > 3) throw new InvalidOptionException("Gamemode",gamemode);
        TablutOptionModel.gamemode = gamemode;
    }

    public static ArrayList<AIMode> getAiMode() {
        return aiMode;
    }

    public static Color getEvenColor() {
        return evenColor;
    }

    public static Color getMainColor() {
        return mainColor;
    }

    public static Color getFontColor() {
        return fontColor;
    }

    public static Color getSecondColor() {
        return secondColor;
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

    public static boolean isCaptureDisabled() {
        return captureDisabled;
    }

    public static boolean isMoveAnywhere() {
        return moveAnywhere;
    }

    public static boolean isMoveAnyPawn() {
        return moveAnyPawn;
    }

    public static boolean isAdvancedColors() {
        return advancedColors;
    }

    public static void setCaptureDisabled(boolean captureDisabled) {
        TablutOptionModel.captureDisabled = captureDisabled;
    }

    public static void setMoveAnywhere(boolean moveAnywhere) {
        TablutOptionModel.moveAnywhere = moveAnywhere;
    }

    public static void setMoveAnyPawn(boolean moveAnyPawn) {
        TablutOptionModel.moveAnyPawn = moveAnyPawn;
    }

    public static void setAiMode(ArrayList<AIMode> aiMode) {
        TablutOptionModel.aiMode = aiMode;
    }

    public static void setMainColor(Color mainColor) {
        TablutOptionModel.mainColor = mainColor;
    }

    public static void setFontColor(Color fontColor) {
        TablutOptionModel.fontColor = fontColor;
    }

    public static void setSecondColor(Color secondColor) {
        TablutOptionModel.secondColor = secondColor;
    }

    public static void setEvenColor(Color evenColor) {
        TablutOptionModel.evenColor = evenColor;
    }

    public static void setOddColor(Color oddColor) {
        TablutOptionModel.oddColor = oddColor;
    }

    public static void setAdvancedColors(boolean advancedColors) {
        TablutOptionModel.advancedColors = advancedColors;
    }

    public static void setKingMode(KingMoveMode kingMode) throws InvalidOptionException {
        TablutOptionModel.kingMode = kingMode;
    }

    public static void setWinMode(WinMode winMode) throws InvalidOptionException {
        TablutOptionModel.winMode = winMode;
    }

    public static boolean isBuilderMode() {
        return builderMode;
    }

    public static void setBuilderMode(boolean builderMode) {
        TablutOptionModel.builderMode = builderMode;
    }

    public static Theme getColorTheme() {
        return colorTheme;
    }

    public static void setColorTheme(Theme colorTheme) {
        TablutOptionModel.colorTheme = colorTheme;
    }

    public static void setMinmaxDepth(int minmaxDepth) {
        TablutOptionModel.minmaxDepth = minmaxDepth;
    }

    public static int getMinmaxDepth() {
        return minmaxDepth;
    }
}