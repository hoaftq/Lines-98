package thbt.webng.com.game.score;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;

class BlockStrategy extends ScoreStrategy {

	private static final int MIN_BLOCK_COUNT_TO_COMPLETE = 7;

	private boolean[][] visited;

	public BlockStrategy(Square[][] squares) {
		super(squares);
	}

	@Override
	public List<Square> getCompletedArea(Position pos) {
		visited = new boolean[getRowCount()][getColCount()];
		var completedSquares = getCompletedBlock(pos, squares[pos.x][pos.y].getBall().getColor());
		return completedSquares.size() >= MIN_BLOCK_COUNT_TO_COMPLETE ? completedSquares : List.of();
	}

	private List<Square> getCompletedBlock(Position pos, Color color) {
		var completedSquares = new ArrayList<Square>();

		if (!visited[pos.x][pos.y]) {
			var square = squares[pos.x][pos.y];
			if (square.isEnableDestroy(color)) {
				completedSquares.add(square);

				if (pos.y > 0) {
					// TODO add methods like left(), right(), etc to Position
					completedSquares.addAll(getCompletedBlock(new Position(pos.x, pos.y - 1), color));
				}

				if (pos.x > 0) {
					completedSquares.addAll(getCompletedBlock(new Position(pos.x - 1, pos.y), color));
				}

				if (pos.y < getRowCount() - 1) {
					completedSquares.addAll(getCompletedBlock(new Position(pos.x, pos.y + 1), color));
				}

				if (pos.x < getColCount() - 1) {
					completedSquares.addAll(getCompletedBlock(new Position(pos.x + 1, pos.y), color));
				}
			}

			visited[pos.x][pos.y] = true;
		}

		return completedSquares;
	}
}
