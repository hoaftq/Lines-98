package thbt.webng.com.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import thbt.webng.com.game.info.GameInfoBoard;
import thbt.webng.com.game.option.GameOptions;
import thbt.webng.com.game.option.GameType;
import thbt.webng.com.game.option.NextBallDisplayType;
import thbt.webng.com.game.sound.SoundManager;
import thbt.webng.com.game.util.ColorUtil;

public class GameBoard {

	public GameBoard(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
		gameInfoBoard = new GameInfoBoard(gamePanel);

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				squareArray[i][j] = new Square(this.gamePanel);
				squareArray[i][j].setLeft(left + j * Square.SIZE);
				squareArray[i][j].setTop(top + i * Square.SIZE);
			}
		}
	}

	public void draw(Graphics g) {
		gameInfoBoard.draw(g);

		boolean displayGrowingBalls = GameOptions.getCurrentInstance()
				.getNextBallDisplayType() == NextBallDisplayType.ShowBoth
				|| GameOptions.getCurrentInstance().getNextBallDisplayType() == NextBallDisplayType.ShowOnField;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				squareArray[i][j].draw(g, displayGrowingBalls);
			}
		}
	}

	public GameInfoBoard getGameInfoBoard() {
		return gameInfoBoard;
	}

	/**
	 * @return size of the game board including game information displayed on the
	 *         top
	 */
	public Dimension getBoardSize() {
		return new Dimension(2 * left + col * Square.SIZE, top + row * Square.SIZE + 1);
	}

	public void newGame(GameType gameType) {
		initBoard();
		addGrowingBall();
		bakGameState = null;
		gameOver = false;

		gameInfoBoard.getNextBallBoard().setNextColors(nextColorArray);
		gameInfoBoard.getClock().setSeconds(0);
		gameInfoBoard.getScore().setScore(0);
		gameInfoBoard.setClockState(true);

		GameOptions.getCurrentInstance().setGameType(gameType);

		gamePanel.repaint();
	}

	public void newGame() {
		newGame(GameOptions.getCurrentInstance().getDefaultGameType());
	}

	public Square getSquare(Position pos) {
		return squareArray[pos.x][pos.y];
	}

	public Position getSelectedPosition() {
		return selectedPos;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void selectBall(Position pos) {
		if (selectedPos != null) {
			getSquare(selectedPos).getBall().unSelect();
		}

		selectedPos = pos;
		getSquare(selectedPos).getBall().select();
	}

	public void stepBack() {
		if (bakGameState != null) {
			restoreGame(bakGameState);
		}
	}

	public void saveGame() {
		savSeconds = gameInfoBoard.getClock().getSeconds();
		saveGame(savGameState = new GameState());
	}

	public void loadGame() {
		if (savGameState != null) {
			gameInfoBoard.getClock().setSeconds(savSeconds);
			restoreGame(savGameState);
			bakGameState = null;
		}
	}

	public Position squareFromMousePos(int x, int y) {
		int i = (y - top) / Square.SIZE;
		int j = (x - left) / Square.SIZE;

		if (y < top || i >= row || x < left || j >= col) {
			return null;
		}

		return new Position(i, j);
	}

	public boolean moveTo(final Position positionTo) {
		if (moveThread != null && moveThread.isAlive()) {
			return false;
		}

		if (selectedPos == null) {
			throw new IllegalStateException("Selected position not null");
		}

		if (selectedPos.equals(positionTo)) {
			return false;
		}

		final List<Position> positionList = findPath(positionTo);
		if (positionList.size() == 0) {
			SoundManager.playCantMoveSound();
			return false;
		}

		if (bakGameState == null) {
			bakGameState = new GameState();
		}
		saveGame(bakGameState);

		final Square squareFrom = squareArray[selectedPos.x][selectedPos.y];
		final Ball ballFrom = squareFrom.getBall();

		ballFrom.unSelect();
		selectedPos = null;
		squareFrom.setBall(null);

		if (GameOptions.getCurrentInstance().isMovementSound()) {
			SoundManager.playMoveSound();
		}

		moveThread = new Thread(() -> {
			Square square = null;
			Ball saveBall;

			for (int i = 1; i < positionList.size(); i++) {
				Position pos = positionList.get(i);
				square = getSquare(pos);

				saveBall = square.getBall();

				square.setBall(new Ball(ballFrom.getColor(), BallState.MATURE, square));

				gamePanel.repaint();

				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				square.setBall(saveBall);
			}

			Ball ballTo = square.getBall();
			square.setBall(ballFrom);

			List<Square> completeSquareList = getCompleteSquare(positionTo);
			if (completeSquareList.size() > 0) {
				explosionBall(completeSquareList);

				if (ballTo != null) {
					if (!square.hasBall()) {
						square.setBall(ballTo);
					} else {
						setNewGrowingPos(positionList.get(positionList.size() - 1), ballTo);
					}
				}
			} else {
				if (ballTo != null) {
					setNewGrowingPos(positionList.get(positionList.size() - 1), ballTo);
				}

				nextStep();
			}

			gamePanel.repaint();
		});

		moveThread.start();

		return true;
	}

	private void explosionBall(List<Square> listCompleteSquare) {
		Ball.hideBall(listCompleteSquare);
		int score = listCompleteSquare.size() + (listCompleteSquare.size() - 4) * listCompleteSquare.size();
		gameInfoBoard.getScore().setScore(gameInfoBoard.getScore().getScore() + score);
	}

	private void setNewGrowingPos(Position oldPos, Ball ball) {
		if (!nextPositionList.remove(oldPos)) {
			throw new IllegalArgumentException();
		}

		List<Position> positionList = getEmptyPositions();
		if (positionList.size() > 0) {
			Position pos = positionList.get(new Random().nextInt(positionList.size()));
			getSquare(pos).setBall(ball);

			nextPositionList.add(pos);
		}
	}

	private void nextStep() {
		List<Square> squareList = new ArrayList<Square>();
		for (Position pos : nextPositionList) {
			squareList.add(getSquare(pos));
		}
		Ball.growBall(squareList);

		for (Position pos : nextPositionList) {
			if (getSquare(pos).hasBall() && getSquare(pos).getBallState() == BallState.MATURE) {
				List<Square> listCompleteSquare = getCompleteSquare(pos);
				if (listCompleteSquare.size() > 0) {
					explosionBall(listCompleteSquare);
				}
			}
		}

		addGrowingBall();
		gameInfoBoard.getNextBallBoard().setNextColors(nextColorArray);

		if (nextPositionList.size() < 3) {
			gameOver = true;
		}
	}

	private void initBoard() {
		if (selectedPos != null) {
			getSquare(selectedPos).getBall().unSelect();
			selectedPos = null;
		}

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				squareArray[i][j].setBall(null);
			}
		}

		List<Position> listEmptyPosition = getEmptyPositions();
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			int idx = random.nextInt(listEmptyPosition.size());
			Square square = getSquare(listEmptyPosition.get(idx));
			square.setBall(new Ball(ColorUtil.getRandomColor(), BallState.MATURE, square));
			listEmptyPosition.remove(idx);
		}
	}

	private void addGrowingBall() {
		generateNextBall();
		generateNextColor();
		for (int i = 0; i < nextPositionList.size(); i++) {
			Square square = getSquare(nextPositionList.get(i));
			square.setBall(new Ball(nextColorArray[i], BallState.GROWING, square));
		}
	}

	private void generateNextBall() {
		nextPositionList = new ArrayList<Position>();
		Random random = new Random();
		List<Position> listEmptySquare = getEmptyPositions();

		for (int i = 0; i < 3; i++) {
			if (listEmptySquare.size() > 0) {
				int idx = random.nextInt(listEmptySquare.size());
				nextPositionList.add(listEmptySquare.get(idx));
				listEmptySquare.remove(idx);
			}
		}
	}

	private List<Position> getEmptyPositions() {
		List<Position> listPosition = new LinkedList<Position>();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (!squareArray[i][j].hasBall()) {
					listPosition.add(new Position(i, j));
				}
			}
		}

		return listPosition;
	}

	private void generateNextColor() {
		nextColorArray[0] = ColorUtil.getRandomColor();
		nextColorArray[1] = ColorUtil.getRandomColor();
		nextColorArray[2] = ColorUtil.getRandomColor();
	}

	private List<Position> findPath(Position positionTo) {
		List<Position> pathList = new LinkedList<Position>();
		Queue<ExtPosition> positionQueue = new LinkedList<ExtPosition>();

		resetVisitedArray();

		positionQueue.add(new ExtPosition(selectedPos.x, selectedPos.y));
		while (positionQueue.size() > 0) {
			Position pos = positionQueue.poll();
			visitedArray[pos.x][pos.y] = true;

			if (pos.x == positionTo.x && pos.y == positionTo.y) {
				do {
					pathList.add(0, pos);
					pos = ((ExtPosition) pos).prevPosition;
				} while (pos != null);

				break;
			}

			positionQueue.addAll(getNeighborsSquare((ExtPosition) pos));
		}

		return pathList;
	}

	private void resetVisitedArray() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				visitedArray[i][j] = false;
			}
		}
	}

	private List<ExtPosition> getNeighborsSquare(ExtPosition pos) {
		List<ExtPosition> positionList = new LinkedList<ExtPosition>();
		int x, y;

		if (pos.x > 0) {
			x = pos.x - 1;
			y = pos.y;
			if (!visitedArray[x][y] && squareArray[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new ExtPosition(x, y, pos));
			}
		}

		if (pos.x < col - 1) {
			x = pos.x + 1;
			y = pos.y;
			if (!visitedArray[x][y] && squareArray[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new ExtPosition(x, y, pos));
			}
		}

		if (pos.y > 0) {
			x = pos.x;
			y = pos.y - 1;
			if (!visitedArray[x][y] && squareArray[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new ExtPosition(x, y, pos));
			}
		}

		if (pos.y < row - 1) {
			x = pos.x;
			y = pos.y + 1;
			if (!visitedArray[x][y] && squareArray[x][y].getBallState() != BallState.MATURE) {
				positionList.add(new ExtPosition(x, y, pos));
			}
		}

		return positionList;
	}

	private List<Square> getCompleteSquare(Position pos) {
		if (GameOptions.getCurrentInstance().getGameType() == GameType.LINE) {
			return getLinesComplete(pos);
		} else if (GameOptions.getCurrentInstance().getGameType() == GameType.SQUARE) {
			return getSquaresComplete(pos);
		} else {
			return getBlocksComplete(pos);
		}
	}

	private List<Square> getLinesComplete(Position pos) {
		List<Square> listCompleteSquare = new ArrayList<Square>();
		Color color = getSquare(pos).getBall().getColor();

		List<Square> listTempSquare;
		Square square;
		int i, j;

		listTempSquare = new ArrayList<Square>();
		j = 1;
		while (pos.y + j < col && (square = squareArray[pos.x][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			j++;
		}
		j = -1;
		while (pos.y + j >= 0 && (square = squareArray[pos.x][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			j--;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		listTempSquare = new ArrayList<Square>();
		i = 1;
		while (pos.x + i < col && (square = squareArray[pos.x + i][pos.y]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i++;
		}
		i = -1;
		while (pos.x + i >= 0 && (square = squareArray[pos.x + i][pos.y]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i--;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		listTempSquare = new ArrayList<Square>();
		i = 1;
		j = 1;
		while (pos.x + i < col && pos.y + j < row
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i++;
			j++;
		}
		i = -1;
		j = -1;
		while (pos.x + i >= 0 && pos.y + j >= 0
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i--;
			j--;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		listTempSquare = new ArrayList<Square>();
		i = 1;
		j = -1;
		while (pos.x + i < col && pos.y + j >= 0
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i++;
			j--;
		}
		i = -1;
		j = 1;
		while (pos.x + i >= 0 && pos.y + j < row
				&& (square = squareArray[pos.x + i][pos.y + j]).isEnableDestroy(color)) {
			listTempSquare.add(square);
			i--;
			j++;
		}
		if (listTempSquare.size() >= 4) {
			listCompleteSquare.addAll(listTempSquare);
		}

		if (listCompleteSquare.size() > 0) {
			listCompleteSquare.add(squareArray[pos.x][pos.y]);
		}

		return listCompleteSquare;
	}

	private List<Square> getSquaresComplete(Position pos) {
		List<Square> listCompleteSquare = new ArrayList<Square>();

		Color color = squareArray[pos.x][pos.y].getBall().getColor();
		boolean b1 = false;
		boolean b2 = false;
		boolean b3 = false;

		if (pos.x > 0 && pos.y > 0) {
			if (squareArray[pos.x][pos.y - 1].isEnableDestroy(color)
					&& squareArray[pos.x - 1][pos.y - 1].isEnableDestroy(color)
					&& squareArray[pos.x - 1][pos.y].isEnableDestroy(color)

			) {
				listCompleteSquare.add(squareArray[pos.x][pos.y - 1]);
				listCompleteSquare.add(squareArray[pos.x - 1][pos.y - 1]);
				listCompleteSquare.add(squareArray[pos.x - 1][pos.y]);
				b1 = true;
			}
		}

		if (pos.x > 0 && pos.y < row - 1) {
			if (squareArray[pos.x - 1][pos.y].isEnableDestroy(color)
					&& squareArray[pos.x - 1][pos.y + 1].isEnableDestroy(color)
					&& squareArray[pos.x][pos.y + 1].isEnableDestroy(color)

			) {
				if (!b1) {
					listCompleteSquare.add(squareArray[pos.x - 1][pos.y]);
				}
				listCompleteSquare.add(squareArray[pos.x - 1][pos.y + 1]);
				listCompleteSquare.add(squareArray[pos.x][pos.y + 1]);
				b2 = true;
			}
		}

		if (pos.x < col - 1 && pos.y < row - 1) {
			if (squareArray[pos.x][pos.y + 1].isEnableDestroy(color)
					&& squareArray[pos.x + 1][pos.y + 1].isEnableDestroy(color)
					&& squareArray[pos.x + 1][pos.y].isEnableDestroy(color)

			) {
				if (!b2) {
					listCompleteSquare.add(squareArray[pos.x][pos.y + 1]);
				}
				listCompleteSquare.add(squareArray[pos.x + 1][pos.y + 1]);
				listCompleteSquare.add(squareArray[pos.x + 1][pos.y]);
				b3 = true;
			}
		}

		if (pos.x < col - 1 && pos.y > 0) {
			if (squareArray[pos.x + 1][pos.y].isEnableDestroy(color)
					&& squareArray[pos.x + 1][pos.y - 1].isEnableDestroy(color)
					&& squareArray[pos.x][pos.y - 1].isEnableDestroy(color)

			) {
				if (!b3) {
					listCompleteSquare.add(squareArray[pos.x + 1][pos.y]);
				}
				listCompleteSquare.add(squareArray[pos.x + 1][pos.y - 1]);

				if (!b1) {
					listCompleteSquare.add(squareArray[pos.x][pos.y - 1]);
				}
			}
		}

		if (listCompleteSquare.size() > 0) {
			listCompleteSquare.add(squareArray[pos.x][pos.y]);
		}

		return listCompleteSquare;
	}

	private List<Square> getBlocksComplete(Position pos) {
		resetVisitedArray();

		List<Square> listCompleteSquare = getBlocksComplete(pos, getSquare(pos).getBall().getColor());

		if (listCompleteSquare.size() >= 7) {
			return listCompleteSquare;
		}

		return new ArrayList<Square>();
	}

	private List<Square> getBlocksComplete(Position pos, Color color) {
		List<Square> listSquare = new ArrayList<Square>();

		if (!visitedArray[pos.x][pos.y]) {
			visitedArray[pos.x][pos.y] = true;

			Square square = getSquare(pos);
			if (square.isEnableDestroy(color)) {
				listSquare.add(square);

				if (pos.y > 0) {
					listSquare.addAll(getBlocksComplete(new Position(pos.x, pos.y - 1), color));
				}

				if (pos.x > 0) {
					listSquare.addAll(getBlocksComplete(new Position(pos.x - 1, pos.y), color));
				}

				if (pos.y < row - 1) {
					listSquare.addAll(getBlocksComplete(new Position(pos.x, pos.y + 1), color));
				}

				if (pos.x < col - 1) {
					listSquare.addAll(getBlocksComplete(new Position(pos.x + 1, pos.y), color));
				}
			}
		}

		return listSquare;
	}

	private void saveGame(GameState gameState) {
		try {
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					if (squareArray[i][j].getBall() != null) {
						gameState.bakBallArray[i][j] = (Ball) (squareArray[i][j].getBall().clone());
					} else {
						gameState.bakBallArray[i][j] = null;
					}
				}
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < 3; i++) {
			gameState.bakNextColorArray[i] = nextColorArray[i];
		}

		gameState.bakNextPositionList = new ArrayList<Position>();
		gameState.bakNextPositionList.addAll(nextPositionList);

		gameState.score = gameInfoBoard.getScore().getScore();
	}

	private void restoreGame(GameState gameState) {
		if (selectedPos != null) {
			getSquare(selectedPos).getBall().unSelect();
			selectedPos = null;
		}

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				squareArray[i][j].setBall(gameState.bakBallArray[i][j]);
			}
		}

		for (int i = 0; i < 3; i++) {
			nextColorArray[i] = gameState.bakNextColorArray[i];
		}

		nextPositionList = gameState.bakNextPositionList;

		gameInfoBoard.getScore().setScore(gameState.score);

		gamePanel.repaint();
	}

	private int row = 9;
	private int col = 9;
	private Square[][] squareArray = new Square[row][col];
	boolean[][] visitedArray = new boolean[row][col];
	private Position selectedPos;

	private int left = 1;
	private int top = 53;

	private Color[] nextColorArray = new Color[3];
	private List<Position> nextPositionList;
	private GamePanel gamePanel;
	private Thread moveThread;

	private GameState bakGameState;
	private GameState savGameState;
	private int savSeconds;

	private GameInfoBoard gameInfoBoard;

	private boolean gameOver;

	private class ExtPosition extends Position {
		public ExtPosition prevPosition;

		public ExtPosition(int x, int y) {
			super(x, y);
		}

		public ExtPosition(int x, int y, ExtPosition prevPosition) {
			super(x, y);
			this.prevPosition = prevPosition;
		}
	}

	private class GameState {
		public Ball[][] bakBallArray = new Ball[row][col];
		public Color[] bakNextColorArray = new Color[3];
		public List<Position> bakNextPositionList;
		public int score;
	}
}
