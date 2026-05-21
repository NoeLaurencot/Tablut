package model;

public class Move {
    public int sourceX;
    public int sourceY;
    public int destX;
    public int destY;

    public Move() {}

    public Move(int sx, int sy, int dx, int dy) {
        sourceX = sx;
        sourceY = sy;
        destX = dx;
        destY = dy;
    }

    public Move(Move m) {
        this.sourceX = m.sourceX;
        this.sourceY = m.sourceY;
        this.destX = m.destX;
        this.destY = m.destY;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof Move m)) {
            return false;
        }
        return this.sourceX == m.sourceX && this.sourceY == m.sourceY
        && this.destX == m.destX && this.destY == m.destY;
    }

    @Override
    public String toString() {
        return (
                "sourceX : " + this.sourceX +
                " sourceY : " + this.sourceY +
                " destX : " + this.destX +
                " destY : " + this.destY
        );
    }
}