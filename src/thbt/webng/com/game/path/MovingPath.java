package thbt.webng.com.game.path;

import thbt.webng.com.game.base.BallState;
import thbt.webng.com.game.base.Position;
import thbt.webng.com.game.base.Square;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MovingPath {

    private final Square[][] squares;
    private boolean[][] visited;

    public MovingPath(Square[][] squares) {
        this.squares = squares;
    }

    public List<Position> findShortestPath(Position positionFrom, Position positionTo) {
        resetVisited();

        Queue<ExtPosition> positionQueue = new LinkedList<>();
        positionQueue.add(new ExtPosition(positionFrom.x(), positionFrom.y()));
        while (positionQueue.size() > 0) {
            var ep = positionQueue.poll();
            if (ep.position.equals(positionTo)) {
                return getPath(ep);
            }

            visited[ep.x()][ep.y()] = true;
            positionQueue.addAll(getNeighborPositions(ep));
        }

        return new ArrayList<>();
    }

    private void resetVisited() {
        visited = new boolean[squares.length][squares[0].length];
    }

    private List<ExtPosition> getNeighborPositions(ExtPosition extPosition) {
        int row = squares.length;
        int col = squares[0].length;

        List<ExtPosition> neighborPositions = new ArrayList<>();

        int[][] directions = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
        for (int[] direction : directions) {
            int x = extPosition.x() + direction[0];
            int y = extPosition.y() + direction[1];

            if (x < 0 || x >= col || y < 0 || y >= row) {
                continue;
            }

            if (visited[x][y] || squares[x][y].getBallState() == BallState.MATURE) {
                continue;
            }

            neighborPositions.add(new ExtPosition(x, y, extPosition));
            visited[x][y] = true;
        }

        return neighborPositions;
    }

    private static LinkedList<Position> getPath(ExtPosition extPosition) {
        var path = new LinkedList<Position>();
        do {
            path.addFirst(extPosition.position);
            extPosition = extPosition.prevExtPosition;
        } while (extPosition != null);

        return path;
    }

    private static class ExtPosition {
        private final Position position;
        private ExtPosition prevExtPosition;

        ExtPosition(int x, int y) {
            position = new Position(x, y);
        }

        ExtPosition(int x, int y, ExtPosition prevExtPosition) {
            this(x, y);
            this.prevExtPosition = prevExtPosition;
        }

        int x() {
            return position.x();
        }

        int y() {
            return position.y();
        }
    }
}
