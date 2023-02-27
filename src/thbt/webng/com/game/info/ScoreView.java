package thbt.webng.com.game.info;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ScoreView {

    private Digit[] digits = new Digit[5];

    public ScoreView() {
        Arrays.setAll(digits, i -> new Digit(12, 11, 3));
    }

    public void draw(Graphics g) {
        for (var digit : digits) {
            digit.draw(g);
        }
    }

    public void setLeft(int left) {
        for (var i = digits.length - 1; i >= 0; i--) {
            digits[i].setLeft(left);
            left += digits[i].getWidth() + 3;
        }
    }

    public void setTop(int top) {
        for (Digit digit : digits) {
            digit.setTop(top);
        }
    }

    public int getLeft() {
        return digits[digits.length - 1].getLeft();
    }

    public int getTop() {
        return digits[0].getTop();
    }

    public int getWidth() {
        return 5 * digits[0].getWidth() + 4 * 3;
    }

    public int getHeight() {
        return digits[0].getHeight();
    }

    void setDigitValues(List<Integer> digitValues) {
        for (var i = 0; i < digits.length; i++) {
            digits[i].setValue(i < digitValues.size() ? digitValues.get(i) : 0);
        }
    }
}
