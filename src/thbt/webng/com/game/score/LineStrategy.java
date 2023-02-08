package thbt.webng.com.game.score;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;

class LineStrategy extends ScoreStrategy {

	private static final int MIN_COMPLETED_SQUARES_COUNT = 5;

	public LineStrategy(Square[][] squares) {
		super(squares);
	}

	@Override
	public List<Square> getCompletedArea(Position pos) {
		var completedSquares = Stream
				.concat(List
						.of(Direction.TO_RIGHT, Direction.TO_BOTTOM, Direction.TO_TOP_RIGHT, Direction.TO_BOTTOM_RIGHT)
						.stream().flatMap(d -> getCompletedLine(pos, d).stream()), Stream.of(squares[pos.x][pos.y]))
				.toList();

		return completedSquares.size() > 1 ? completedSquares : List.of();
	}

	private List<Square> getCompletedLine(Position pos, Direction dir) {
		Color color = squares[pos.x][pos.y].getBall().getColor();
		var lineSquares = new ArrayList<Square>();

		for (var d : new Direction[] { dir, dir.opposite() }) {
			var i = d.getX();
			var j = d.getY();
			while (pos.x + i >= 0 && pos.x + i < getColCount() && pos.y + j >= 0 && pos.y + j < getRowCount()
					&& squares[pos.x + i][pos.y + j].isEnableDestroy(color)) {
				lineSquares.add(squares[pos.x + i][pos.y + j]);
				i += d.getX();
				j += d.getY();
			}
		}

		return lineSquares.size() >= MIN_COMPLETED_SQUARES_COUNT - 1 ? lineSquares : List.of();
	}
}
