package thbt.webng.com.game.score;

import thbt.webng.com.game.base.Position;
import thbt.webng.com.game.base.Square;

import java.util.Collection;
import java.util.List;

public abstract class ScoreStrategy {

    protected Square[][] squares;

    public ScoreStrategy(Square[][] squares) {
        this.squares = squares;
    }

    public int getRowCount() {
        return squares.length;
    }

    public int getColCount() {
        return squares[0].length;
    }

    public abstract List<Square> getCompletedArea(Position pos);

    protected boolean isValidPosition(int x, int y) {
        return x >= 0 && x < getColCount() && y >= 0 && y < getRowCount();
    }

    protected boolean isValidPosition(Position pos) {
        return isValidPosition(pos.x, pos.y);
    }

    protected List<Square> mapPositionsToSquares(Collection<Position> positions) {
        return positions.stream().map(p -> squares[p.x][p.y]).toList();
    }
}
