package thbt.webng.com.game.score;

import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class LineStrategy extends ScoreStrategy {

    private static final int MIN_COMPLETED_SQUARES_COUNT = 5;

    public LineStrategy(Square[][] squares) {
        super(squares);
    }

    @Override
    public List<Square> getCompletedArea(Position pos) {
        var completedPositions = Stream.concat(
                List.of(Direction.TO_RIGHT, Direction.TO_BOTTOM, Direction.TO_TOP_RIGHT, Direction.TO_BOTTOM_RIGHT)
                        .stream().flatMap(d -> getCompletedLine(pos, d).stream()),
                Stream.of(pos)).toList();

        return completedPositions.size() > 1 ? mapPositionsToSquares(completedPositions) : List.of();
    }

    private List<Position> getCompletedLine(Position pos, Direction dir) {
        Color color = squares[pos.x][pos.y].getBall().getColor();
        var linePositions = new ArrayList<Position>();

        for (var d : new Direction[]{dir, dir.opposite()}) {
            var i = d.getX();
            var j = d.getY();
            while (isValidPosition(pos.x + i, pos.y + j) && squares[pos.x + i][pos.y + j].isDestroyable(color)) {
                linePositions.add(new Position(pos.x + i, pos.y + j));
                i += d.getX();
                j += d.getY();
            }
        }

        return linePositions.size() >= MIN_COMPLETED_SQUARES_COUNT - 1 ? linePositions : List.of();
    }
}
