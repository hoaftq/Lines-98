package thbt.webng.com.game.scorehistory;

import thbt.webng.com.game.util.StorageUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerScoreHistory {

    private final static int SCORE_HISTORY_LIMIT = 10;
    private final static String SCORE_HISTORY_FILE_NAME = "ScoreHistory";
    private static PlayerScoreHistory instance = new PlayerScoreHistory();
    private List<PlayerScore> scores;

    private PlayerScoreHistory() {
        scores = StorageUtil.<List<PlayerScore>>load(SCORE_HISTORY_FILE_NAME).orElse(new ArrayList<>());
    }

    public static PlayerScoreHistory getInstance() {
        return instance;
    }

    public int getHighestScore() {
        return scores.stream().mapToInt(s -> s.score()).max().orElse(0);
    }

    public List<PlayerScore> getTopScores() {
        return scores;
    }

    public boolean isNewRecord(int score) {
        return score > 0 && (scores.size() < SCORE_HISTORY_LIMIT || scores.stream().anyMatch(ps -> ps.score() < score));
    }

    public void addHighScore(PlayerScore playerScore) {
        scores = Stream.concat(scores.stream(), Stream.of(playerScore))
                .sorted(Comparator.comparingInt(PlayerScore::score).reversed()).limit(SCORE_HISTORY_LIMIT)
                .collect(Collectors.toList());
    }

    public void save() {
        StorageUtil.save(scores, SCORE_HISTORY_FILE_NAME);
    }
}
