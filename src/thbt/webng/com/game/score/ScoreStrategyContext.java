package thbt.webng.com.game.score;

import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;
import thbt.webng.com.game.option.GameOptions;

import java.util.List;

public class ScoreStrategyContext {

    public List<Square> getCompleteArea(Square[][] squares, Position pos) {
        var scoreStrategy = getScoreStrategy(squares);
        return scoreStrategy.getCompletedArea(pos);
    }

    private ScoreStrategy getScoreStrategy(Square[][] squares) {
        return switch (GameOptions.getCurrentInstance().getGameType()) {
            case LINE -> new LineStrategy(squares);
            case SQUARE -> new SquareStrategy(squares);
            case BLOCK -> new BlockStrategy(squares);
            default -> throw new IllegalStateException();
        };
    }
}
