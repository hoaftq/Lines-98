package thbt.webng.com.game.status;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerScoreHistory {

	private final static int SCORE_HISTORY_LIMIT = 10;

	private final static String SCORE_HISTORY_FILE_NAME = "ScoreHistory";

	private List<PlayerScore> scores;

	private static PlayerScoreHistory instance = new PlayerScoreHistory();

	private PlayerScoreHistory() {
		try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(SCORE_HISTORY_FILE_NAME))) {
			scores = (List<PlayerScore>) stream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			scores = new ArrayList<>();
			e.printStackTrace();
		}
	}

	public static PlayerScoreHistory getInstance() {
		return instance;
	}

	public int getHighestScore() {
		return scores.stream().mapToInt(s -> s.getScore()).max().orElse(0);
	}

	public List<PlayerScore> getTopScores() {
		return scores;
	}

	public boolean isNewRecord(int score) {
		return (scores.size() < SCORE_HISTORY_LIMIT) || (scores.stream().anyMatch(ps -> ps.getScore() < score));
	}

	public void addHighScore(PlayerScore playerScore) {
		scores = Stream.concat(scores.stream(), Stream.of(playerScore))
				.sorted(Comparator.comparingInt(PlayerScore::getScore).reversed()).limit(SCORE_HISTORY_LIMIT)
				.collect(Collectors.toList());
	}

	public void save() {
		try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(SCORE_HISTORY_FILE_NAME))) {
			stream.writeObject(scores);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
