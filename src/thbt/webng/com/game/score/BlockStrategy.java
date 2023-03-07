package thbt.webng.com.game.score;

import thbt.webng.com.game.base.Position;
import thbt.webng.com.game.base.Square;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class BlockStrategy extends ScoreStrategy {

    private static final int MIN_COMPLETED_SQUARES_COUNT = 7;

    private boolean[][] visited;

    public BlockStrategy(Square[][] squares) {
        super(squares);
    }

    @Override
    public List<Square> getCompletedArea(Position pos) {
        visited = new boolean[getRowCount()][getColCount()];
        var completedSquares = getCompletedBlock(pos, squares[pos.x()][pos.y()].getBall().getColor());
        return completedSquares.size() >= MIN_COMPLETED_SQUARES_COUNT ? mapPositionsToSquares(completedSquares)
                : List.of();
    }

    private List<Position> getCompletedBlock(Position pos, Color color) {
        var completedPositions = new ArrayList<Position>();

        if (!visited[pos.x()][pos.y()]) {
            visited[pos.x()][pos.y()] = true;

            if (squares[pos.x()][pos.y()].isDestroyable(color)) {
                completedPositions.add(pos);

                if (pos.y() > 0) {
                    // TODO add methods like left(), right(), etc to Position
                    completedPositions.addAll(getCompletedBlock(new Position(pos.x(), pos.y() - 1), color));
                }

                if (pos.x() > 0) {
                    completedPositions.addAll(getCompletedBlock(new Position(pos.x() - 1, pos.y()), color));
                }

                if (pos.y() < getRowCount() - 1) {
                    completedPositions.addAll(getCompletedBlock(new Position(pos.x(), pos.y() + 1), color));
                }

                if (pos.x() < getColCount() - 1) {
                    completedPositions.addAll(getCompletedBlock(new Position(pos.x() + 1, pos.y()), color));
                }
            }
        }

        return completedPositions;
    }
}
