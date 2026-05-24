package model;

public class Coordinate {
    protected int x;
    protected int y;

    public Coordinate() {
    }

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

    public void increment(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public Coordinate invertedCopy() {
        return new Coordinate(this.y, this.x);
    }

    @Override
    public String toString() {
        return "X:" + this.x + " Y: " + this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Coordinate c)) {
            return false;
        }
        return this.x == c.x && this.y == c.y;
    }
}

