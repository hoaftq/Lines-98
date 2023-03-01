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
    private final static PlayerScoreHistory instance = new PlayerScoreHistory();
    private List<PlayerScore> playerScores;

    private PlayerScoreHistory() {
        playerScores = StorageUtil.<List<PlayerScore>>load(SCORE_HISTORY_FILE_NAME).orElse(new ArrayList<>());
    }

    public static PlayerScoreHistory getInstance() {
        return instance;
    }

    public int getHighestScore() {
        return playerScores.stream().mapToInt(PlayerScore::score).max().orElse(0);
    }

    public List<PlayerScore> getTopScores() {
        return playerScores;
    }

    public boolean isNewRecord(int score) {
        return score > 0
                && (playerScores.size() < SCORE_HISTORY_LIMIT || playerScores.stream().anyMatch(ps -> ps.score() < score));
    }

    public void addHighScore(PlayerScore playerScore) {
        playerScores = Stream.concat(playerScores.stream(), Stream.of(playerScore))
                .sorted(Comparator.comparingInt(PlayerScore::score).reversed())
                .limit(SCORE_HISTORY_LIMIT)
                .collect(Collectors.toList());
    }

    public void save() {
        StorageUtil.save(playerScores, SCORE_HISTORY_FILE_NAME);
    }
}
