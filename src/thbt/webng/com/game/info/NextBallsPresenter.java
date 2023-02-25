package thbt.webng.com.game.info;

import java.awt.*;

public class NextBallsPresenter {

    private NextBallsModel model;

    private NextBallsView view;

    public NextBallsPresenter(NextBallsModel model, NextBallsView view) {
        this.model = model;
        this.view = view;
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

    public int getWidth() {
        return view.getWidth();
    }

    public int getHeight() {
        return view.getHeight();
    }

    public void generateNextColors() {
        model.generateNextColors();
        view.setNextColors(model.getNextColors());
    }

    public void setNextColors(Color[] nextColors) {
        model.setNextColors(nextColors);
        view.setNextColors(nextColors);
    }

    public Color[] getNextColors() {
        return model.getNextColors();
    }
}
