package thbt.webng.com.game.info;

import thbt.webng.com.game.base.BaseBall;

import java.awt.*;
import java.util.Arrays;

public class NextBallsView {

    private BaseBall[] nextBalls = new BaseBall[3];

    public NextBallsView() {
        Arrays.setAll(nextBalls, i -> new BaseBall(23, 23));
    }

    public void draw(Graphics g) {
        for (var ball : nextBalls) {
            ball.draw(g);
        }
    }

    public void setLeft(int left) {
        for (var ball : nextBalls) {
            ball.setLeft(left);
            left += ball.getWidth() + 6;
        }
    }

    public void setTop(int top) {
        for (var ball : nextBalls) {
            ball.setTop(top);
        }
    }

    public int getWidth() {
        return nextBalls[0].getWidth() * 3 + 6 * 2;
    }

    public int getHeight() {
        return nextBalls[0].getHeight();
    }

    public void setNextColors(Color[] colors) {
        if (nextBalls.length != colors.length) {
            throw new IllegalArgumentException("colors must have %d items".formatted(nextBalls.length));
        }

        for (var i = 0; i < nextBalls.length; i++) {
            nextBalls[i].setColor(colors[i]);
        }
    }
}
