package model;

public class TablutOptionModel {

    // mode 0 : Human vs Human
    // mode 1 : Human vs Computer
    // mode 2 : Computer vs Human
    // mode 3 : Computer vs Computer
    private int gamemode;

    public TablutOptionModel() {
        this.gamemode = 1;
    };

    public int getGamemode() {
        return this.gamemode;
    }

    public void setGamemode(int gamemode) throws InvalidIntegerOptionException  {
        if (gamemode < 0 || gamemode > 3) throw new InvalidIntegerOptionException("Gamemode",gamemode);
        this.gamemode = gamemode;
    }

}

class InvalidIntegerOptionException extends Exception {
    private String invalidOption;
    private int integerOption;

    public InvalidIntegerOptionException(String invalidOption,int integerOption) {
        this.integerOption = integerOption;
        this.invalidOption = invalidOption;
    }
    @Override
    public String toString() {
        return ("Error : " + this.invalidOption + "can't take " + this.integerOption + "as a value !!!");
    }
}
