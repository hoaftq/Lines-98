package thbt.webng.com.game.score;

import java.util.List;

import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;

public abstract class ScoreStrategy {

	protected Square[][] squareArray;

	public ScoreStrategy(Square[][] squareArray) {
		this.squareArray = squareArray;
	}

	abstract List<Square> getCompleteArea(Position pos);
}
