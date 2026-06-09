package model;

public class CoordinateByte {
    private byte x;
    private byte y;

    public CoordinateByte() {}

    public CoordinateByte(byte x, byte y) {
        this.x = x;
        this.y = y;
    }

    public CoordinateByte(CoordinateByte C) {
        this.x = C.getX();
        this.y = C.getY();
    }

    public CoordinateByte(Coordinate C) {
        this.x = (byte)C.getX();
        this.y = (byte)C.getY();
    }

    public byte getX() {
        return this.x;
    }

    public void setX(byte x) {
        this.x = x;
    }

    public byte getY() {
        return this.y;
    }

    public void setY(byte y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " x = " + this.x + " y = " + this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof CoordinateByte C)) {
            return false;
        }
        return this.x == C.getX() && this.y == C.getY();
    }
}