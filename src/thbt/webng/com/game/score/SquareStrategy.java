package thbt.webng.com.game.score;

import thbt.webng.com.game.base.Position;
import thbt.webng.com.game.base.Square;

import java.awt.*;
import java.util.HashSet;
import java.util.List;

class SquareStrategy extends ScoreStrategy {

    public SquareStrategy(Square[][] squares) {
        super(squares);
    }

    @Override
    public List<Square> getCompletedArea(Position pos) {
        var completedSquares = new HashSet<Position>();
        completedSquares.addAll(
                getCompletedPositions(pos, List.of(Direction.TO_TOP, Direction.TO_RIGHT, Direction.TO_TOP_RIGHT)));
        completedSquares.addAll(getCompletedPositions(pos,
                List.of(Direction.TO_BOTTOM, Direction.TO_RIGHT, Direction.TO_BOTTOM_RIGHT)));
        completedSquares.addAll(
                getCompletedPositions(pos, List.of(Direction.TO_BOTTOM, Direction.TO_LEFT, Direction.TO_BOTTOM_LEFT)));
        completedSquares.addAll(
                getCompletedPositions(pos, List.of(Direction.TO_TOP, Direction.TO_LEFT, Direction.TO_TOP_LEFT)));

        if (completedSquares.size() > 0) {
            completedSquares.add(pos);
            return mapPositionsToSquares(completedSquares);
        }

        return List.of();
    }

    private List<Position> getCompletedPositions(Position pos, List<Direction> directions) {
        Color color = squares[pos.x][pos.y].getBall().getColor();
        var completedSquares = directions.stream().map(d -> new Position(pos.x + d.getX(), pos.y + d.getY()))
                .filter(SquareStrategy.this::isValidPosition).filter(p -> squares[p.x][p.y].isDestroyable(color))
                .toList();
        return completedSquares.size() == 3 ? completedSquares : List.of();
    }
}
