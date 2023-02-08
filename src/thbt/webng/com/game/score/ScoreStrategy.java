package thbt.webng.com.game.score;

import java.util.List;

import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;

public abstract class ScoreStrategy {

	protected Square[][] squares;

	public int getRowCount() {
		return squares.length;
	}

	public int getColCount() {
		return squares[0].length;
	}

	public ScoreStrategy(Square[][] squares) {
		this.squares = squares;
	}

	abstract List<Square> getCompletedArea(Position pos);
}
