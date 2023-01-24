package thbt.webng.com.game.info;

import java.io.Serializable;

public record PlayerScore(String name, int score, String playTime) implements Serializable {
}
