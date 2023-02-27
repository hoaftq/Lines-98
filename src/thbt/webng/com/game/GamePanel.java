package thbt.webng.com.game;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import thbt.webng.com.GameFrame;
import thbt.webng.com.game.board.GameBoardModel;
import thbt.webng.com.game.board.GameBoardPresenter;
import thbt.webng.com.game.board.GameBoardView;
import thbt.webng.com.game.info.GameInfoPresenter;
import thbt.webng.com.game.option.GameType;

public class GamePanel extends JPanel {
	private static final long serialVersionUID = -5724271697960761395L;

	private GameFrame gameFrame;
	private GameBoardPresenter presenter;

	public GamePanel(GameFrame frame) {
		gameFrame = frame;
		presenter = new GameBoardPresenter(new GameBoardModel(), new GameBoardView(this));

		initialize();
	}

	public void initialize() {
		setPreferredSize(presenter.getBoardSize());

		presenter.newGame();

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				if (e.getButton() == MouseEvent.BUTTON1) {
					presenter.playAt(e.getX(), e.getY());

					if (presenter.isGameOver()) {
						gameFrame.endGame();
					}
				}
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		presenter.draw(g);
	}

	public void newGame() {
		presenter.newGame();
	}

	public void newGame(GameType gameType) {
		presenter.newGame(gameType);
	}

	public void saveGame() {
		presenter.saveGame();
	}

	public void loadGame() {
		presenter.loadGame();
	}

	public void stepBack() {
		presenter.stepBack();
	}

	public GameInfoPresenter getGameInfoBoard() {
		return presenter.getGameInfoBoard();
	}
}
