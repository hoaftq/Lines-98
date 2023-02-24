package thbt.webng.com.game.info;

import thbt.webng.com.game.GamePanel;
import thbt.webng.com.game.option.GameOptions;
import thbt.webng.com.game.option.NextBallDisplayType;

import java.awt.*;

public class GameInfoBoard {

	private int left = 1;
	private int top = 2;
	private int width = 45 * 9;
	private int height = 50;

	private Score highestScore = new Score();
	private Score score = new Score();
	private DigitalClockPresenter clock;
	private NextBallBoard nextBallBoard = new NextBallBoard();

	public GameInfoBoard(GamePanel gamePanel) {
		highestScore.setLeft(10);
		highestScore.setTop(top + (height - highestScore.getHeight()) / 2);

		score.setLeft(left + width - score.getWidth() - 10);
		score.setTop(top + (height - score.getHeight()) / 2);

		clock = new DigitalClockPresenter(new DigitalClockModel(), new DigitalClockView(gamePanel));
		clock.setLeft(left + (width - clock.getWidth()) / 2);
		clock.setTop(36);
		clock.start();

		nextBallBoard.setLeft(left + (width - nextBallBoard.getWidth()) / 2);
		nextBallBoard.setTop(11);
		nextBallBoard.setNextColors(new Color[] { Color.BLACK, Color.BLACK, Color.BLACK });

		new Thread(() -> highestScore.setScore(PlayerScoreHistory.getInstance().getHighestScore())).start();
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fill3DRect(left, top, width, height, true);
		highestScore.draw(g);
		score.draw(g);

		NextBallDisplayType displayType = GameOptions.getCurrentInstance().getNextBallDisplayType();
		if (displayType == NextBallDisplayType.ShowBoth || displayType == NextBallDisplayType.ShowOnTop) {
			nextBallBoard.draw(g);
		}

		drawGameType(g);
		clock.draw(g);
	}

	public Score getHighestScore() {
		return highestScore;
	}

	public Score getScore() {
		return score;
	}

	// TODO shouldn't we expose this class? create wrapper methods instead
	public DigitalClockPresenter getClock() {
		return clock;
	}

	public NextBallBoard getNextBallBoard() {
		return nextBallBoard;
	}

	public void setClockState(boolean isRun) {
		if (isRun) {
			clock.start();
		} else {
			clock.stop();
		}

	}

	private void drawGameType(Graphics g) {
		String gameTypeString = GameOptions.getCurrentInstance().getGameType().toString().toUpperCase();

		g.setFont(g.getFont().deriveFont(Font.BOLD, 7f));

		int w = g.getFontMetrics().stringWidth(gameTypeString);

		g.setColor(new Color(0, 96, 191));
		g.drawString(gameTypeString, left + (width - w) / 2, top + 8);
	}
}
