package thbt.webng.com.game;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class Square {

	public Square(JComponent component) {
		this.component = component;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;

		if (this.ball != null) {
			this.ball.square = this;
		}
	}

	public boolean hasBall() {
		return ball != null;
	}

	public BallState getBallState() {
		if (ball == null) {
			return BallState.Removed;
		}

		return ball.getBallState();
	}

	public boolean isEnableDestroy(Color color) {
		if (ball == null) {
			return false;
		}

		return ball.getBallState() == BallState.Maturity
				&& ball.getColor().equals(color);
	}

	public void draw(Graphics g) {
		drawBackground(g);

		if (ball != null) {
			ball.draw(g);
		}
	}

	public void repaint() {
		component.repaint(left, top, size, size);
	}

	private void drawBackground(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fill3DRect(left, top, SIZE, SIZE, true);
	}

	private int left;
	private int top;
	private int size = SIZE;
	private Ball ball;

	private JComponent component;

	public static final int SIZE = 45;
}
