package model;

public enum Theme {
    DARK,
    LIGHT;

    @Override
    public String toString() {
        if (this == DARK) {
            return "Dark";
        } else {
            return "Light";
        }
    }
}
