package thbt.webng.com.game.base;

import javax.swing.*;
import java.awt.*;

public class Square {
    private static final int DEFAULT_SIZE = 45;
    private final JComponent component;
    private int left;
    private int top;
    private int size = DEFAULT_SIZE;
    private Ball ball;

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
        return ball == null ? BallState.REMOVED : ball.getBallState();
    }

    public boolean isDestroyable(Color color) {
        return ball != null
                && ball.getBallState() == BallState.MATURE && ball.getColor().equals(color);
    }

    public void draw(Graphics g, boolean showGrowingBalls) {
        drawBackground(g);

        if (ball == null) {
            return;
        }

        if (showGrowingBalls) {
            ball.draw(g);
        } else if (ball.getBallState() != BallState.GROWING) {
            ball.draw(g);
        }
    }

    public void repaint() {
        component.repaint(left, top, size, size);
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        g.fill3DRect(left, top, DEFAULT_SIZE, DEFAULT_SIZE, true);
    }
}
