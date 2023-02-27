package thbt.webng.com.game.info;

import java.awt.*;

public class ScorePresenter {

    private ScoreModel model;

    private ScoreView view;

    public ScorePresenter(ScoreModel model, ScoreView view) {
        this.model = model;
        this.view = view;
    }

    public ScoreView getView() {
        return view;
    }

    public void draw(Graphics g) {
        view.draw(g);
    }

    public void setLeft(int left) {
        view.setLeft(left);
    }

    public void setTop(int top) {
        view.setTop(top);
    }

    public int getScore() {
        return model.getScore();
    }

    public void setScore(int score) {
        model.setScore(score);
        view.setDigitValues(model.getDigitValues());
    }

    public int getWidth() {
        return view.getWidth();
    }

    public int getHeight() {
        return view.getHeight();
    }
}
