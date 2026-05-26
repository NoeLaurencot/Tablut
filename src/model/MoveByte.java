package model;

public class MoveByte {
    public byte sourceX;
    public byte sourceY;
    public byte destX;
    public byte destY;

    public MoveByte() {}

    public MoveByte(byte sx, byte sy, byte dx, byte dy) {
        sourceX = sx;
        sourceY = sy;
        destX = dx;
        destY = dy;
    }

    public MoveByte(MoveByte m) {
        this.sourceX = m.sourceX;
        this.sourceY = m.sourceY;
        this.destX = m.destX;
        this.destY = m.destY;
    }

    public MoveByte(Move m) {
        this.sourceX = (byte)m.sourceX;
        this.sourceY = (byte)m.sourceY;
        this.destX = (byte)m.destX;
        this.destY = (byte)m.destY;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof MoveByte m)) {
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