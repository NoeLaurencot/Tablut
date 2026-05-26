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

    public Move(MoveByte mb) {
        this.sourceX = (int)mb.sourceX;
        this.sourceY = (int)mb.sourceY;
        this.destX = (int)mb.destX;
        this.destY = (int)mb.destY;
    }

    public Move reverse() {
        return new Move(destX, destY, sourceX, sourceY);
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
                "[X:" + this.sourceX +
                " Y:" + this.sourceY +
                "] -> [X:" + this.destX +
                " Y:" + this.destY + "]"
        );
    }

    public String humanReadable() {
        char[] charTable = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        return ("" + charTable[this.sourceY] + (this.sourceX+1) + " -> " + charTable[this.destY] + (this.destX+1));
    }
}