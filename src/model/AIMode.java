package model;

public enum AIMode {
    MINMAX,
    SIMPLE_EVAL;

    @Override
    public String toString() {
        if (this.equals(MINMAX))
            return "Minmax";
        else if (this.equals(SIMPLE_EVAL))
            return "Simple";
        else
            return super.toString();
    }
}
