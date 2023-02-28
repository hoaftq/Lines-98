package thbt.webng.com.game.info;

import thbt.webng.com.game.option.GameOptionsManager;
import thbt.webng.com.game.option.NextBallsDisplayType;

import java.awt.*;

public class GameInfoView {
    private static final int LEFT = 1;
    private static final int TOP = 2;
    private static final int WIDTH = 45 * 9;
    private static int HEIGHT = 50;
    private final ScoreView scoreView;
    private final ScoreView highestScoreView;
    private final DigitalClockView digitalClockView;
    private final NextBallsView nextBallsView;

    public GameInfoView(ScoreView scoreView, ScoreView highestScoreView, DigitalClockView digitalClockView, NextBallsView nextBallsView) {
        this.scoreView = scoreView;
        this.highestScoreView = highestScoreView;
        this.digitalClockView = digitalClockView;
        this.nextBallsView = nextBallsView;

        positionViews();
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fill3DRect(LEFT, TOP, WIDTH, HEIGHT, true);
        highestScoreView.draw(g);
        scoreView.draw(g);

        NextBallsDisplayType displayType = GameOptionsManager.getCurrentGameOptions().getNextBallsDisplayTypes();
        if (displayType == NextBallsDisplayType.ShowBoth || displayType == NextBallsDisplayType.ShowOnTop) {
            nextBallsView.draw(g);
        }

        drawGameType(g);
        digitalClockView.draw(g);
    }

    private void positionViews() {
        highestScoreView.setLeft(LEFT + 9);
        highestScoreView.setTop(TOP + (HEIGHT - highestScoreView.getHeight()) / 2);

        scoreView.setLeft(LEFT + WIDTH - scoreView.getWidth() - highestScoreView.getLeft());
        scoreView.setTop(TOP + (HEIGHT - scoreView.getHeight()) / 2);

        digitalClockView.setLeft(LEFT + (WIDTH - digitalClockView.getWidth()) / 2);
        digitalClockView.setTop(TOP + 34);

        nextBallsView.setLeft(LEFT + (WIDTH - nextBallsView.getWidth()) / 2);
        nextBallsView.setTop(TOP + 9);
    }

    private void drawGameType(Graphics g) {
        g.setFont(g.getFont().deriveFont(Font.BOLD, 7f));

        var gameType = GameOptionsManager.getCurrentGameOptions().getGameType().toString().toUpperCase();
        int gameTypeWidth = g.getFontMetrics().stringWidth(gameType);
        g.setColor(new Color(0, 96, 191));
        g.drawString(gameType, LEFT + (WIDTH - gameTypeWidth) / 2, TOP + 8);
    }
}
