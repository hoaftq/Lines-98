package thbt.webng.com.game.info;

import java.io.Serializable;

public class PlayerScore implements Serializable {

	private static final long serialVersionUID = 6544769094170780400L;

	private String name;

	private int score;

	private String playTime;

	public PlayerScore(String name, int score, String playTime) {
		this.name = name;
		this.score = score;
		this.playTime = playTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getPlayTime() {
		return playTime;
	}

	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}

}
