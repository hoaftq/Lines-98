package thbt.webng.com.game.info;

import thbt.webng.com.game.util.ColorUtil;

import java.awt.*;

public class NextBallsModel {

    private Color[] nextColors = new Color[3]; // TODO

    public Color[] getNextColors() {
        return nextColors;
    }

    public void setNextColors(Color[] nextColors) {
        this.nextColors = nextColors;
    }

    public void generateNextColors() {
        nextColors = new Color[]{ColorUtil.getRandomColor(), ColorUtil.getRandomColor(), ColorUtil.getRandomColor()};
    }
}
