package thbt.webng.com.game.score;

import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;
import thbt.webng.com.game.option.GameOptions;
import thbt.webng.com.game.option.GameOptionsManager;

import java.util.List;

public class ScoreStrategyContext {

    public List<Square> getCompleteArea(Square[][] squares, Position pos) {
        var scoreStrategy = getScoreStrategy(squares);
        return scoreStrategy.getCompletedArea(pos);
    }

    private ScoreStrategy getScoreStrategy(Square[][] squares) {
        return switch (GameOptionsManager.getCurrentGameOptions().getGameType()) {
            case LINE -> new LineStrategy(squares);
            case SQUARE -> new SquareStrategy(squares);
            case BLOCK -> new BlockStrategy(squares);
            default -> throw new IllegalStateException();
        };
    }
}
