package thbt.webng.com.game.info;
import java.awt.Graphics;

public class Score {

	public Score() {
		for (int i = 0; i < digits.length; i++) {
			digits[i] = new Digit(12, 11, 3);
		}
	}

	public void draw(Graphics g) {
		for (int i = 0; i < digits.length; i++) {
			digits[i].draw(g);
		}
	}

	public void setLeft(int left) {
		digits[4].setLeft(left);
		left += digits[4].getWidth() + 3;
		digits[3].setLeft(left);
		left += digits[3].getWidth() + 3;
		digits[2].setLeft(left);
		left += digits[2].getWidth() + 3;
		digits[1].setLeft(left);
		left += digits[1].getWidth() + 3;
		digits[0].setLeft(left);
	}

	public void setTop(int top) {
		for (Digit digit : digits) {
			digit.setTop(top);
		}
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;

		int i = 0;
		while (i < digits.length) {
			digits[i].setValue(score % 10);
			score /= 10;
			i++;
		}
	}

	public int getWidth() {
		return 5 * digits[0].getWidth() + 4 * 3;
	}

	public int getHeight() {
		return digits[0].getHeight();
	}

	private int score;
	private Digit[] digits = new Digit[5];
}
