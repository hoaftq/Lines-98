package thbt.webng.com.game.board;

import thbt.webng.com.game.base.Ball;
import thbt.webng.com.game.base.BallState;
import thbt.webng.com.game.base.Position;
import thbt.webng.com.game.base.Square;
import thbt.webng.com.game.option.GameOptionsManager;
import thbt.webng.com.game.path.MovingPath;
import thbt.webng.com.game.score.ScoreStrategyContext;
import thbt.webng.com.game.sound.SoundManager;
import thbt.webng.com.game.util.ColorUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class GameBoardModel {

    private final int row = 9;
    private final int col = 9;
    private final Square[][] squares = new Square[row][col];
    private GameBoardModelListener modelListener;
    private Position selectedPosition;
    private List<Position> nextBallPositions;
    private Thread moveThread;
    private boolean gameOver;
    private GameState previousGameState;
    private GameState savedGameState;

    public void setModelListener(GameBoardModelListener modelListener) {
        this.modelListener = modelListener;
    }

    public Square getSquareAt(Position pos) {
        return squares[pos.x][pos.y];
    }

    public Square[][] getSquares() {
        return squares;
    }

    public Position getSelectedPosition() {
        return selectedPosition;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void newGame() {
        clearGameBoard();
        addFirstBalls();
        addGrowingBalls();

        modelListener.onModelChanged();
    }

    public void selectBall(Position position) {
        unselectBall();

        selectedPosition = position;
        getSquareAt(position).getBall().select();
    }

    public void moveTo(Position positionTo) {
        if (moveThread != null && moveThread.isAlive()) {
            return;
        }

        if (selectedPosition == null) {
            throw new IllegalStateException("Selected position is required to move");
        }

        if (selectedPosition.equals(positionTo)) {
            return;
        }

        var movingPositions = new MovingPath(squares).findPath(selectedPosition, positionTo);
        if (movingPositions.isEmpty()) {
            SoundManager.playCantMoveSound();
            return;
        }

        previousGameState = takeGameSnapshot();

        var ballFrom = removeSelectedBall();

        if (GameOptionsManager.getCurrentGameOptions().isPlayMoveSound()) {
            SoundManager.playMoveSound();
        }

        startMovingThread(positionTo, movingPositions, ballFrom);
    }


    public void stepBack() {
        if (previousGameState != null) {
            restoreGame(previousGameState, false);
        }
    }

    public void saveGame() {
        savedGameState = takeGameSnapshot();
    }

    public void loadGame() {
        if (savedGameState != null) {
            restoreGame(savedGameState, true);
            previousGameState = null;
        }
    }

    private Ball removeSelectedBall() {
        var selectedSquare = getSquareAt(selectedPosition);
        var selectedBall = selectedSquare.getBall();

        selectedBall.unSelect();
        selectedPosition = null;
        selectedSquare.setBall(null);

        return selectedBall;
    }

    private void startMovingThread(Position positionTo, List<Position> movingPositions, Ball ballFrom) {
        moveThread = new Thread(() -> {
            var overriddenBall = moveSelectedBall(ballFrom, movingPositions);

            var completedSquares = getCompleteArea(positionTo);
            if (!completedSquares.isEmpty()) {
                explodeBalls(completedSquares);
                handleOverriddenBall(positionTo, overriddenBall);
            } else {
                handleOverriddenBall(positionTo, overriddenBall);
                growBallsAndAddNewGrowingBalls();
            }

            modelListener.onModelChanged();
        });
        moveThread.start();
    }


    private Ball moveSelectedBall(Ball ballFrom, List<Position> movingPositions) {
        for (int i = 1; i < movingPositions.size(); i++) {
            var square = getSquareAt(movingPositions.get(i));

            var currentBall = square.getBall();
            square.setBall(new Ball(ballFrom.getColor(), BallState.MATURE, square));

            modelListener.onModelChanged();

            try {
                sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            square.setBall(currentBall);
        }

        var positionTo = movingPositions.get(movingPositions.size() - 1);
        var squareTo = getSquareAt(positionTo);
        var ballTo = squareTo.getBall();
        squareTo.setBall(ballFrom);
        return ballTo;
    }

    private void handleOverriddenBall(Position positionTo, Ball overriddenBall) {
        if (overriddenBall == null) {
            return;
        }

        var targetSquare = getSquareAt(positionTo);
        if (!targetSquare.hasBall()) {
            targetSquare.setBall(overriddenBall);
        } else {
            setNewGrowingPos(positionTo, overriddenBall);
        }
    }

    private void explodeBalls(List<Square> completedSquares) {
        hideBalls(completedSquares);
        int score = completedSquares.size() + (completedSquares.size() - 4) * completedSquares.size();
        modelListener.setScore(modelListener.getScore() + score);
    }

    private void setNewGrowingPos(Position oldPos, Ball ball) {
        if (!nextBallPositions.remove(oldPos)) {
            throw new IllegalArgumentException();
        }

        List<Position> positionList = getEmptyPositions();
        if (positionList.size() > 0) {
            Position pos = positionList.get(new Random().nextInt(positionList.size()));
            getSquareAt(pos).setBall(ball);

            nextBallPositions.add(pos);
        }
    }

    private void growBallsAndAddNewGrowingBalls() {
        growBalls();
        addGrowingBalls();
    }

    private void clearGameBoard() {
        unselectBall();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                squares[i][j].setBall(null);
            }
        }

        previousGameState = null;
        gameOver = false;
    }

    private void addFirstBalls() {
        List<Position> listEmptyPosition = getEmptyPositions();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int idx = random.nextInt(listEmptyPosition.size());
            Square square = getSquareAt(listEmptyPosition.get(idx));
            square.setBall(new Ball(ColorUtil.getRandomColor(), BallState.MATURE, square));
            listEmptyPosition.remove(idx);
        }
    }

    private void addGrowingBalls() {
        nextBallPositions = getNextBallPositions();
        gameOver = nextBallPositions.size() < 3; // TODO ?

        modelListener.generateNextColors();
        var nextColors = modelListener.getNextColors();

        for (int i = 0; i < nextBallPositions.size(); i++) {
            var square = getSquareAt(nextBallPositions.get(i));
            square.setBall(new Ball(nextColors[i], BallState.GROWING, square));
        }
    }

    private List<Position> getNextBallPositions() {
        var nextBallPositions = new ArrayList<Position>();
        var random = new Random();
        var emptyPositions = getEmptyPositions();
        for (int i = 0; i < 3 && emptyPositions.size() > 0; i++) {
            int index = random.nextInt(emptyPositions.size());
            nextBallPositions.add(emptyPositions.remove(index));
        }

        return nextBallPositions;
    }

    private List<Position> getEmptyPositions() {
        List<Position> emptyPositions = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (!squares[i][j].hasBall()) {
                    emptyPositions.add(new Position(i, j));
                }
            }
        }

        return emptyPositions;
    }

    private List<Square> getCompleteArea(Position pos) {
        return new ScoreStrategyContext().getCompleteArea(squares, pos);
    }

    private void growBalls() {
        var growingBallSquares = nextBallPositions.stream().map(this::getSquareAt).toList();
        if (growingBallSquares.stream().anyMatch(s -> s.getBall().getBallState() != BallState.GROWING)) {
            throw new IllegalStateException();
        }

        while (growingBallSquares.get(0).getBallState() != BallState.MATURE) {
            for (var square : growingBallSquares) {
                square.getBall().grow();
            }

            try {
                sleep(GameOptionsManager.getCurrentGameOptions().getAppearanceStepDelay());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (var position : nextBallPositions) {
            var square = getSquareAt(position);
            if (square.hasBall() && square.getBallState() == BallState.MATURE) {
                var completedSquares = getCompleteArea(position);
                if (!completedSquares.isEmpty()) {
                    explodeBalls(completedSquares);
                }
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
                sleep(GameOptionsManager.getCurrentGameOptions().getExplosionStepDelay());
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

    private GameState takeGameSnapshot() {
        var gameState = new GameState();
        try {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (squares[i][j].getBall() != null) {
                        gameState.balls[i][j] = (Ball) (squares[i][j].getBall().clone());
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
            gameState.nextBallColors[i] = modelListener.getNextColors()[i];
        }

        gameState.nextBallPositions = new ArrayList<>(nextBallPositions);

        gameState.score = modelListener.getScore();
        gameState.spentTime = modelListener.getSpentTime();

        return gameState;
    }

    private void restoreGame(GameState gameState, boolean withSpentTime) {
        unselectBall();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                squares[i][j].setBall(gameState.balls[i][j]);
            }
        }

        modelListener.setNextColors(gameState.nextBallColors);

        nextBallPositions = gameState.nextBallPositions;

        modelListener.setScore(gameState.score);

        if (withSpentTime) {
            modelListener.setSpentTime(gameState.spentTime);
        }

        modelListener.onModelChanged();
    }

    private void unselectBall() {
        if (selectedPosition == null) {
            return;
        }

        getSquareAt(selectedPosition).getBall().unSelect();
        selectedPosition = null;
    }

    private class GameState {
        Ball[][] balls = new Ball[row][col];
        Color[] nextBallColors = new Color[3];
        List<Position> nextBallPositions;
        int score;
        int spentTime;
    }
}
