package thbt.webng.com.game.info;

import java.awt.Color;
import java.awt.Graphics;

import thbt.webng.com.game.common.PrimitiveBall;

public class NextBallBoard {

	public NextBallBoard() {
		for (int i = 0; i < nextBallArray.length; i++) {
			nextBallArray[i] = new PrimitiveBall(23, 23);
		}

	}

	public void draw(Graphics g) {
		for (PrimitiveBall ball : nextBallArray) {
			ball.draw(g);
		}
	}

	public int getWidth() {
		return nextBallArray[0].getWidth() * 3 + 6 * 2;
	}

	public int getHeight() {
		return nextBallArray[0].getHeight();
	}

	public void setNextColors(Color[] colors) {
		for (int i = 0; i < nextBallArray.length; i++) {
			nextBallArray[i].setColor(colors[i]);
		}
	}

	public void setLeft(int left) {
		nextBallArray[0].setLeft(left);
		left += nextBallArray[0].getWidth() + 6;
		nextBallArray[1].setLeft(left);
		left += nextBallArray[1].getWidth() + 6;
		nextBallArray[2].setLeft(left);
	}

	public void setTop(int top) {
		for (PrimitiveBall ball : nextBallArray) {
			ball.setTop(top);
		}
	}

	private PrimitiveBall[] nextBallArray = new PrimitiveBall[3];
}
