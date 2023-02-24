package thbt.webng.com.game.info;

import javax.swing.*;
import java.awt.*;

public class DigitalClockPresenter {
    private DigitalClockModel model;
    private DigitalClockView view;

    private Timer clockTimer = new Timer(1000, (e) -> {
        setTimeInSeconds(getTimeInSeconds() + 1);
    });

    public DigitalClockPresenter(DigitalClockModel model, DigitalClockView view) {
        this.model = model;
        this.view = view;
    }

    public void start() {
        if (clockTimer.isRunning()) {
            return;
        }

        clockTimer.start();
    }

    public void stop() {
        clockTimer.stop();
    }

    public void draw(Graphics g) {
        view.draw(g);
    }

    public int getTimeInSeconds() {
        return model.getTimeInSeconds();
    }

    public void setTimeInSeconds(int timeInSecond) {
        model.setTimeInSeconds(timeInSecond);
        var digitValues = model.getDigitValues();

        view.setDigitValues(digitValues);
    }

    public void resetTime() {
        setTimeInSeconds(0);
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
}
