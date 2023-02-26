package thbt.webng.com.game.scorehistory;

import java.io.Serializable;

public record PlayerScore(String name, int score, String playTime) implements Serializable {
}
