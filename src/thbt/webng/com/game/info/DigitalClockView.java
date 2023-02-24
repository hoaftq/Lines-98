package thbt.webng.com.game.info;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class DigitalClockView {
    private JPanel panel;

    private Digit[] digits = new Digit[5];

    public DigitalClockView(JPanel panel) {
        this.panel = panel;
        Arrays.setAll(digits, i -> new Digit(5, 3, 1));
    }

    public int getLeft() {
        return digits[4].getLeft();
    }

    public int getTop() {
        return digits[0].getTop();
    }

    public void draw(Graphics g) {
        for (Digit digit : digits) {
            digit.draw(g);
        }

        var oneQuarterHeight = digits[0].getHeight() / 4;

        // Draw a colon between hour and minute
        drawColon(g, digits[4].getWidth() + 1, oneQuarterHeight, oneQuarterHeight * 2);

        // Draw a colon between minute and second
        drawColon(g, 3 * digits[4].getWidth() + 3 + 2 + 1, oneQuarterHeight, oneQuarterHeight * 2);
    }

    public void setDigitValues(int[] digitValues) {
        if (digits.length != digitValues.length) {
            throw new IllegalArgumentException("digitValues must have %d items".formatted(digits.length));
        }

        for (int i = 0; i < digits.length; i++) {
            digits[i].setValue(digitValues[i]);
        }

        panel.repaint(getLeft(), getTop(), getWidth(), getHeight());
    }

    public void setLeft(int left) {
        for (var i = digits.length - 1; i >= 0; i--) {
            digits[i].setLeft(left);
            left += digits[i].getWidth() + (i % 2 == 0 ? 3 : 2);
        }
    }

    public void setTop(int top) {
        for (var digit : digits) {
            digit.setTop(top);
        }
    }

    public int getWidth() {
        return digits[0].getWidth() * 5 + 3 * 2 + 2 * 2;
    }

    public int getHeight() {
        return digits[0].getHeight();
    }

    private void drawColon(Graphics g, int offsetLeft, int offsetTop, int height) {
        g.fillRect(getLeft() + offsetLeft, getTop() + offsetTop, 1, 1);
        g.fillRect(getLeft() + offsetLeft, getTop() + offsetTop + height, 1, 1);
    }
}
