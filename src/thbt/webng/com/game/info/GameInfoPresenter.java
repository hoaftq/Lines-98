package thbt.webng.com.game.info;

import thbt.webng.com.game.scorehistory.PlayerScoreHistory;

import javax.swing.*;
import java.awt.*;

public class GameInfoPresenter {
    private final GameInfoView view;

    private final ScorePresenter highestScorePresenter;
    private final ScorePresenter scorePresenter;
    private final DigitalClockPresenter digitalClockPresenter;
    private final NextBallsPresenter nextBallsPresenter;

    public GameInfoPresenter(JPanel gamePanel) {
        scorePresenter = new ScorePresenter(new ScoreModel(), new ScoreView());
        highestScorePresenter = new ScorePresenter(new ScoreModel(), new ScoreView());
        nextBallsPresenter = new NextBallsPresenter(new NextBallsModel(), new NextBallsView());
        digitalClockPresenter = new DigitalClockPresenter(new DigitalClockModel(), new DigitalClockView(gamePanel));
        view = new GameInfoView(
                scorePresenter.getView(),
                highestScorePresenter.getView(),
                digitalClockPresenter.getView(),
                nextBallsPresenter.getView());

        digitalClockPresenter.start();
        new Thread(() -> highestScorePresenter.setScore(PlayerScoreHistory.getInstance().getHighestScore())).start();
    }

    public void draw(Graphics g) {
        view.draw(g);
    }

    public ScorePresenter getHighestScorePresenter() {
        return highestScorePresenter;
    }

    public ScorePresenter getScorePresenter() {
        return scorePresenter;
    }

    public DigitalClockPresenter getDigitalClockPresenter() {
        return digitalClockPresenter;
    }

    public NextBallsPresenter getNextBallsPresenter() {
        return nextBallsPresenter;
    }
}
