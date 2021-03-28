package thbt.webng.com.game;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import thbt.webng.com.GameFrame;

public class GamePanel extends JPanel {

	public GamePanel(GameFrame gameFrame) {
		this.gameFrame = gameFrame;
	}

	public void setGameBoard(final GameBoard gameBoard) {
		this.gameBoard = gameBoard;

		gameBoard.newGame();

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				if (e.getButton() == MouseEvent.BUTTON1) {
					Position pos = gameBoard.squareFromMousePos(e.getX(),
							e.getY());
					if (pos == null) {
						return;
					}

					Square square = gameBoard.getSquare(pos);

					if (square.getBallState() == BallState.MATURE) {
						gameBoard.selectBall(pos);
					} else {
						if (gameBoard.getSelectedPosition() != null) {
							if (gameBoard.moveTo(pos)) {
							}
						}
					}

					if (gameBoard.isGameOver()) {
						gameFrame.endGame();
					}
				}
			}
		});
	}

	public GameBoard getGameBoard() {
		return gameBoard;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		gameBoard.draw(g);
	}

	private GameBoard gameBoard;
	private GameFrame gameFrame;

	private static final long serialVersionUID = -5724271697960761395L;
}
