package thbt.webng.com.game.board;

import java.awt.Dimension;
import java.awt.Graphics;

import thbt.webng.com.game.BallState;
import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;
import thbt.webng.com.game.info.GameInfoPresenter;
import thbt.webng.com.game.option.GameOptions;
import thbt.webng.com.game.option.GameType;

public class GameBoardPresenter {

	private GameBoardModel model;

	private GameBoardView view;

	public GameBoardPresenter(GameBoardModel model, GameBoardView view) {
		this.model = model;
		this.view = view;
	}

	public void newGame(GameType gameType) {
		view.newGame(gameType);
	}

	public void newGame() {
		newGame(GameOptions.getCurrentInstance().getDefaultGameType());
	}

	public void playAt(int mouseX, int mouseY) {
		Position pos = view.squareFromMousePos(mouseX, mouseY);
		if (pos == null) {
			return;
		}

		Square square = view.getSquare(pos);

		if (square.getBallState() == BallState.MATURE) {
			view.selectBall(pos);
		} else {
			if (view.getSelectedPosition() != null) {
				if (view.moveTo(pos)) {
				}
			}
		}
	}

	public boolean isGameOver() {
		return view.isGameOver();
	}

	public Dimension getBoardSize() {
		return view.getBoardSize();
	}

	public void draw(Graphics g) {
		view.draw(g);
	}

	public void saveGame() {
		view.saveGame();
	}

	public void loadGame() {
		view.loadGame();
	}

	public void stepBack() {
		view.stepBack();
	}

	public GameInfoPresenter getGameInfoBoard() {
		return view.getGameInfoBoard();
	}

}
