package thbt.webng.com.game.base;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position pos)) {
            return false;
        }

        return x == pos.x && y == pos.y;
    }

    @Override
    public int hashCode() {
        return x * 1000 + y;
    }
}