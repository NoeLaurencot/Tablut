package model;

public class InvalidIntegerOptionException extends Exception {
    private final String invalidOption;
    private final int integerOption;

    public InvalidIntegerOptionException(String invalidOption,int integerOption) {
        this.integerOption = integerOption;
        this.invalidOption = invalidOption;
    }
    @Override
    public String toString() {
        return ("Error : " + this.invalidOption + "can't take " + this.integerOption + "as a value !!!");
    }
}
