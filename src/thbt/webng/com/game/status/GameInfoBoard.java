package thbt.webng.com.game.status;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Timer;

import thbt.webng.com.game.GamePanel;
import thbt.webng.com.game.option.GameInfo;

public class GameInfoBoard {

	public GameInfoBoard(GamePanel gamePanel) {
		this.gamePanel = gamePanel;

		highestScore.setLeft(10);
		highestScore.setTop(top + (height - highestScore.getHeight()) / 2);

		score.setLeft(left + width - score.getWidth() - 10);
		score.setTop(top + (height - score.getHeight()) / 2);

		clock.setLeft(left + (width - clock.getWidth()) / 2);
		clock.setTop(35);

		nextBallBoard.setLeft(left + (width - nextBallBoard.getWidth()) / 2);
		nextBallBoard.setTop(10);
		nextBallBoard.setNextColors(new Color[] { Color.BLACK, Color.BLACK, Color.BLACK });

		clockTimer.start();

		new Thread(() -> highestScore.setScore(PlayerScoreHistory.getInstance().getHighestScore())).start();
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fill3DRect(left, top, width, height, true);
		highestScore.draw(g);
		score.draw(g);
		nextBallBoard.draw(g);
		drawGameType(g);
		clock.draw(g);
	}

	public Score getHighestScore() {
		return highestScore;
	}

	public Score getScore() {
		return score;
	}

	public DigitalClock getClock() {
		return clock;
	}

	public NextBallBoard getNextBallBoard() {
		return nextBallBoard;
	}

	public void setClockState(boolean isRun) {
		if (isRun) {
			if (!clockTimer.isRunning()) {
				clockTimer.start();
			}
		} else {
			clockTimer.stop();
		}
	}

	private void drawGameType(Graphics g) {
		String gameTypeString = GameInfo.getCurrentInstance().getGameType().toString().toUpperCase();

		g.setFont(g.getFont().deriveFont(Font.BOLD, 7f));

		int w = g.getFontMetrics().stringWidth(gameTypeString);

		g.setColor(new Color(0, 96, 191));
		g.drawString(gameTypeString, left + (width - w) / 2, top + 6);
	}

	private int left = 1;
	private int top = 2;
	private int width = 45 * 9;
	private int height = 50;

	private GamePanel gamePanel;

	private Score highestScore = new Score();
	private Score score = new Score();
	private DigitalClock clock = new DigitalClock();
	private NextBallBoard nextBallBoard = new NextBallBoard();

	private Timer clockTimer = new Timer(1000, (e) -> {
		clock.setSeconds(clock.getSeconds() + 1);
		gamePanel.repaint(left, top, width, height);
	});
}
