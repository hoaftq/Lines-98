package thbt.webng.com.game.info;

import java.util.ArrayList;
import java.util.List;

public class ScoreModel {

    private int score;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    List<Integer> getDigitValues() {
        var digitValues = new ArrayList<Integer>();
        var remainingScore = score;

        while (remainingScore > 0) {
            digitValues.add(remainingScore % 10);
            remainingScore /= 10;
        }

        return digitValues;
    }
}
