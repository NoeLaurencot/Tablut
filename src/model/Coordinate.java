package model;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate() {}

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate C) {
        this.x = C.getX();
        this.y = C.getY();
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
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
        if (!(o instanceof Coordinate C)) {
            return false;
        }
        return this.x == C.getX() && this.y == C.getY();
    }
}