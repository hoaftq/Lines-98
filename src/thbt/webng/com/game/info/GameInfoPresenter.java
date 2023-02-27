package thbt.webng.com.game.info;

import thbt.webng.com.game.GamePanel;
import thbt.webng.com.game.scorehistory.PlayerScoreHistory;

import java.awt.*;

public class GameInfoPresenter {
    private GameInfoView view;

    private ScorePresenter highestScorePresenter;
    private ScorePresenter scorePresenter;
    private DigitalClockPresenter digitalClockPresenter;
    private NextBallsPresenter nextBallsPresenter;

    public GameInfoPresenter(GamePanel gamePanel) {
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
