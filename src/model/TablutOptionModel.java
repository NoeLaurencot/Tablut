package model;

import javafx.scene.paint.Color;

public class TablutOptionModel {

    // mode 0 : Human vs Human
    // mode 1 : Human vs Computer
    // mode 2 : Computer vs Human
    // mode 3 : Computer vs Computer
    private int gamemode;
    // mode 0 edges
    // mode 1 corners
    private int winMode;
    // mode 0 long move
    // mode 1 short move
    private int kingMode;

    Color oddColor;
    Color evenColor;


    public TablutOptionModel() {
        this.gamemode = 1;
        this.winMode = 0;
        this.kingMode = 0;
        this.oddColor = Color.rgb(194, 172, 151);
        this.evenColor = Color.BEIGE;
    };

    public int getGamemode() {
        return this.gamemode;
    }

    public void setGamemode(int gamemode) throws InvalidIntegerOptionException  {
        if (gamemode < 0 || gamemode > 3) throw new InvalidIntegerOptionException("Gamemode",gamemode);
        this.gamemode = gamemode;
    }

    public Color getEvenColor() {
        return evenColor;
    }

    public Color getOddColor() {
        return oddColor;
    }

    public int getKingMode() {
        return kingMode;
    }

    public int getWinMode() {
        return winMode;
    }

    public void setEvenColor(Color evenColor) {
        this.evenColor = evenColor;
    }

    public void setOddColor(Color oddColor) {
        this.oddColor = oddColor;
    }

    public void setKingMode(int kingMode) throws InvalidIntegerOptionException {
        if (kingMode < 0 || kingMode > 1) throw new InvalidIntegerOptionException("King Mode",kingMode);
        this.kingMode = kingMode;
    }

    public void setWinMode(int winMode) throws InvalidIntegerOptionException {
        if (winMode < 0 || winMode > 1) throw new InvalidIntegerOptionException("Win Mode",winMode);
        this.winMode = winMode;
    }
}