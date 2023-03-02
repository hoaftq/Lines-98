package thbt.webng.com.game.board;

import thbt.webng.com.game.*;
import thbt.webng.com.game.info.GameInfoPresenter;
import thbt.webng.com.game.info.NextBallsPresenter;
import thbt.webng.com.game.option.GameOptionsManager;
import thbt.webng.com.game.option.GameType;
import thbt.webng.com.game.option.NextBallsDisplayType;
import thbt.webng.com.game.path.MovingPath;
import thbt.webng.com.game.score.ScoreStrategyContext;
import thbt.webng.com.game.sound.SoundManager;
import thbt.webng.com.game.util.ColorUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GameBoardView {

    private int row = 9;
    private int col = 9;
    private Square[][] squareArray = new Square[row][col];
    private Position selectedPos;
    private int left = 1;
    private int top = 53;
    private List<Position> nextBallPositions;
    private GamePanel gamePanel;
    private Thread moveThread;
    private GameInfoPresenter gameInfoBoard;
    private boolean gameOver;
    private GameState previousGameState;
    private GameState savedGameState;

    public GameBoardView(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        gameInfoBoard = new GameInfoPresenter(gamePanel);

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

        boolean displayGrowingBalls = GameOptionsManager.getCurrentGameOptions()
                .getNextBallsDisplayTypes() == NextBallsDisplayType.ShowBoth
                || GameOptionsManager.getCurrentGameOptions().getNextBallsDisplayTypes() == NextBallsDisplayType.ShowOnField;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                squareArray[i][j].draw(g, displayGrowingBalls);
            }
        }
    }

    public GameInfoPresenter getGameInfoBoard() {
        return gameInfoBoard;
    }

    /**
     * @return size of the game board including game information displayed on the
     * top
     */
    public Dimension getBoardSize() {
        return new Dimension(2 * left + col * Square.SIZE, top + row * Square.SIZE + 1);
    }

    public void newGame(GameType gameType) {
        initBoard();
        addGrowingBall();
        previousGameState = null;
        gameOver = false;

        gameInfoBoard.getDigitalClockPresenter().resetTime();
        gameInfoBoard.getDigitalClockPresenter().start();
        gameInfoBoard.getScorePresenter().setScore(0);

        GameOptionsManager.getCurrentGameOptions().setGameType(gameType);

        gamePanel.repaint();
    }

    public void newGame() {
        newGame(GameOptionsManager.getCurrentGameOptions().getDefaultGameType());
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
        if (previousGameState != null) {
            restoreGame(previousGameState, false);
        }
    }

    public void saveGame() {
        saveGame(savedGameState = new GameState());
    }

    public void loadGame() {
        if (savedGameState != null) {
            restoreGame(savedGameState, true);
            previousGameState = null;
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

        final List<Position> positionList = new MovingPath(squareArray).findPath(selectedPos, positionTo);
        if (positionList.size() == 0) {
            SoundManager.playCantMoveSound();
            return false;
        }

        if (previousGameState == null) {
            previousGameState = new GameState();
        }
        saveGame(previousGameState);

        final Square squareFrom = squareArray[selectedPos.x][selectedPos.y];
        final Ball ballFrom = squareFrom.getBall();

        ballFrom.unSelect();
        selectedPos = null;
        squareFrom.setBall(null);

        if (GameOptionsManager.getCurrentGameOptions().isPlayMoveSound()) {
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

            List<Square> completeSquareList = getCompleteArea(positionTo);
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
        hideBalls(listCompleteSquare);
        int score = listCompleteSquare.size() + (listCompleteSquare.size() - 4) * listCompleteSquare.size();
        gameInfoBoard.getScorePresenter().setScore(gameInfoBoard.getScorePresenter().getScore() + score);
    }

    private void setNewGrowingPos(Position oldPos, Ball ball) {
        if (!nextBallPositions.remove(oldPos)) {
            throw new IllegalArgumentException();
        }

        List<Position> positionList = getEmptyPositions();
        if (positionList.size() > 0) {
            Position pos = positionList.get(new Random().nextInt(positionList.size()));
            getSquare(pos).setBall(ball);

            nextBallPositions.add(pos);
        }
    }

    private void nextStep() {
        List<Square> squareList = new ArrayList<Square>();
        for (Position pos : nextBallPositions) {
            squareList.add(getSquare(pos));
        }
        growBalls(squareList);

        for (Position pos : nextBallPositions) {
            if (getSquare(pos).hasBall() && getSquare(pos).getBallState() == BallState.MATURE) {
                List<Square> listCompleteSquare = getCompleteArea(pos);
                if (listCompleteSquare.size() > 0) {
                    explosionBall(listCompleteSquare);
                }
            }
        }

        addGrowingBall();

        if (nextBallPositions.size() < 3) {
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
        getNextBalls().generateNextColors();

        for (int i = 0; i < nextBallPositions.size(); i++) {
            Square square = getSquare(nextBallPositions.get(i));
            square.setBall(new Ball(getNextBalls().getNextColors()[i], BallState.GROWING, square));
        }
    }

    private void generateNextBall() {
        nextBallPositions = new ArrayList<Position>();
        Random random = new Random();
        List<Position> listEmptySquare = getEmptyPositions();

        for (int i = 0; i < 3; i++) {
            if (listEmptySquare.size() > 0) {
                int idx = random.nextInt(listEmptySquare.size());
                nextBallPositions.add(listEmptySquare.get(idx));
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

    private List<Square> getCompleteArea(Position pos) {
        return new ScoreStrategyContext().getCompleteArea(squareArray, pos);
    }

    private void saveGame(GameState gameState) {
        try {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (squareArray[i][j].getBall() != null) {
                        gameState.balls[i][j] = (Ball) (squareArray[i][j].getBall().clone());
                    } else {
                        gameState.balls[i][j] = null;
                    }
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 3; i++) {
            // TODO
            gameState.nextBallColors[i] = getNextBalls().getNextColors()[i];
        }

        gameState.nextBallPositions = new ArrayList<Position>();
        gameState.nextBallPositions.addAll(nextBallPositions);

        gameState.score = gameInfoBoard.getScorePresenter().getScore();
        gameState.spentTime = gameInfoBoard.getDigitalClockPresenter().getTimeInSeconds();
    }

    private void restoreGame(GameState gameState, boolean withSpentTime) {
        if (selectedPos != null) {
            getSquare(selectedPos).getBall().unSelect();
            selectedPos = null;
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                squareArray[i][j].setBall(gameState.balls[i][j]);
            }
        }

        getNextBalls().setNextColors(gameState.nextBallColors);

        nextBallPositions = gameState.nextBallPositions;

        gameInfoBoard.getScorePresenter().setScore(gameState.score);

        if (withSpentTime) {
            gameInfoBoard.getDigitalClockPresenter().setTimeInSeconds(gameState.spentTime);
        }

        gamePanel.repaint();
    }

    private NextBallsPresenter getNextBalls() {
        return getGameInfoBoard().getNextBallsPresenter();
    }

    private static void growBalls(final List<Square> squareList) {
        if (squareList.stream().anyMatch(s -> s.getBall().getBallState() != BallState.GROWING)) {
            throw new IllegalStateException();
        }

        while (squareList.get(0).getBallState() != BallState.MATURE) {
            for (var square : squareList) {
                square.getBall().grow();
            }

            try {
                Thread.sleep(GameOptionsManager.getCurrentGameOptions().getAppearanceStepDelay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void hideBalls(final List<Square> squareList) {
        if (squareList.stream().anyMatch(s -> s.getBall().getBallState() != BallState.MATURE)) {
            throw new IllegalStateException();
        }


        while (squareList.get(0).getBall().getBallState() != BallState.REMOVED) {
            for (Square square : squareList) {
                square.getBall().shrink();
            }

            try {
                Thread.sleep(GameOptionsManager.getCurrentGameOptions().getExplosionStepDelay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Square square : squareList) {
            square.setBall(null);
        }

        if (GameOptionsManager.getCurrentGameOptions().isPlayDestroySound()) {
            SoundManager.playDestroySound();
        }
    }

    private class GameState {
        Ball[][] balls = new Ball[row][col];
        Color[] nextBallColors = new Color[3];
        List<Position> nextBallPositions;
        int score;
        int spentTime;
    }
}
