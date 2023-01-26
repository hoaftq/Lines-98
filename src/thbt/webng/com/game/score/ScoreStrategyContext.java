package thbt.webng.com.game.score;

import java.util.List;

import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;
import thbt.webng.com.game.option.GameOptions;
import thbt.webng.com.game.option.GameType;

public class ScoreStrategyContext {

	public List<Square> getCompleteArea(Square[][] squareArray, Position pos) {
		var scoreStrategy = getScoreStrategy(squareArray);
		return scoreStrategy.getCompleteArea(pos);
	}

	private ScoreStrategy getScoreStrategy(Square[][] squareArray) {
		if (GameOptions.getCurrentInstance().getGameType() == GameType.LINE) {
			return new LineStrategy(squareArray);
		} else if (GameOptions.getCurrentInstance().getGameType() == GameType.SQUARE) {
			return new SquareStrategy(squareArray);
		} else {
			return new BlockStrategy(squareArray);
		}
	}
}
