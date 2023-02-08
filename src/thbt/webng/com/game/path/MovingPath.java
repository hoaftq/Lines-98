package thbt.webng.com.game.path;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import thbt.webng.com.game.BallState;
import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;

public class MovingPath {

	private Square[][] squares;
	private boolean[][] visitedArray;
	
	public MovingPath(Square[][] squares) {
		this.squares = squares;
	}

	public List<Position> findPath(Position selectedPos, Position positionTo) {
		List<Position> pathList = new LinkedList<Position>();
		Queue<ExtPosition> positionQueue = new LinkedList<ExtPosition>();

//		resetVisitedArray();
		visitedArray = new boolean[squares.length][squares[0].length];

		positionQueue.add(new ExtPosition(selectedPos.x, selectedPos.y));
		while (positionQueue.size() > 0) {
			Position pos = positionQueue.poll();
			visitedArray[pos.x][pos.y] = true;

			if (pos.x == positionTo.x && pos.y == positionTo.y) {
				do {
					pathList.add(0, pos);
					pos = ((ExtPosition) pos).prevPosition;
				} while (pos != null);

				break;
			}

			positionQueue.addAll(getNeighborsSquare((ExtPosition) pos));
		}

		return pathList;
	}

//	private void resetVisitedArray() {
//		for (int i = 0; i < row; i++) {
//			for (int j = 0; j < col; j++) {
//				visitedArray[i][j] = false;
//			}
//		}
//	}

	private List<ExtPosition> getNeighborsSquare(ExtPosition pos) {
		int row = squares.length;
		int col = squares[0].length;

		List<ExtPosition> positionList = new LinkedList<ExtPosition>();
		int x, y;

		if (pos.x > 0) {
			x = pos.x - 1;
			y = pos.y;
			if (!visitedArray[x][y] && squares[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new ExtPosition(x, y, pos));
			}
		}

		if (pos.x < col - 1) {
			x = pos.x + 1;
			y = pos.y;
			if (!visitedArray[x][y] && squares[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new ExtPosition(x, y, pos));
			}
		}

		if (pos.y > 0) {
			x = pos.x;
			y = pos.y - 1;
			if (!visitedArray[x][y] && squares[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new ExtPosition(x, y, pos));
			}
		}

		if (pos.y < row - 1) {
			x = pos.x;
			y = pos.y + 1;
			if (!visitedArray[x][y] && squares[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new ExtPosition(x, y, pos));
			}
		}

		return positionList;
	}

	private class ExtPosition extends Position {
		public ExtPosition prevPosition;

		public ExtPosition(int x, int y) {
			super(x, y);
		}

		public ExtPosition(int x, int y, ExtPosition prevPosition) {
			super(x, y);
			this.prevPosition = prevPosition;
		}
	}
}
