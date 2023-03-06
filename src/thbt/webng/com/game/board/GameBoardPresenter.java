package thbt.webng.com.game.board;

import thbt.webng.com.game.base.BallState;
import thbt.webng.com.game.info.GameInfoPresenter;
import thbt.webng.com.game.option.GameOptionsManager;
import thbt.webng.com.game.option.GameType;

import java.awt.*;

public class GameBoardPresenter implements GameBoardModelListener, GameBoardViewListener {

    private final GameBoardModel model;

    private final GameBoardView view;

    private final GameInfoPresenter gameInfoPresenter;

    public GameBoardPresenter(GameBoardModel model, GameBoardView view, GameInfoPresenter gameInfoPresenter) {
        this.model = model;
        this.view = view;
        this.gameInfoPresenter = gameInfoPresenter;

        this.model.setModelListener(this);
        this.view.setViewListener(this);

        newGame();
    }

    public void newGame(GameType gameType) {
        model.newGame();

        gameInfoPresenter.getDigitalClockPresenter().resetTime();
        gameInfoPresenter.getDigitalClockPresenter().start();
        gameInfoPresenter.getScorePresenter().setScore(0);

        GameOptionsManager.getCurrentGameOptions().setGameType(gameType);
    }

    public void newGame() {
        newGame(GameOptionsManager.getCurrentGameOptions().getDefaultGameType());
    }

    public void playAt(int mouseX, int mouseY) {
        var position = view.getPositionAtMouse(mouseX, mouseY);
        if (position == null) {
            return;
        }

        var square = model.getSquareAt(position);
        if (square.getBallState() == BallState.MATURE) {
            model.selectBall(position);
        } else if (model.getSelectedPosition() != null) {
            model.moveTo(position);
        }
    }

    public boolean isGameOver() {
        return model.isGameOver();
    }

    public void saveGame() {
        model.saveGame();
    }

    public void loadGame() {
        model.loadGame();
    }

    public void stepBack() {
        model.stepBack();
    }

    public void repaint() {
        view.repaint();
    }

    public GameInfoPresenter getGameInfoBoard() {
        return gameInfoPresenter;
    }

    @Override
    public void onModelChanged() {
        view.repaint();
    }

    @Override
    public void onPlayAt(int mouseX, int mouseY) {
        playAt(mouseX, mouseY);
    }

    @Override
    public int getScore() {
        return gameInfoPresenter.getScorePresenter().getScore();
    }

    @Override
    public int getSpentTime() {
        return gameInfoPresenter.getDigitalClockPresenter().getTimeInSeconds();
    }

    @Override
    public void setScore(int score) {
        gameInfoPresenter.getScorePresenter().setScore(score);
    }

    @Override
    public void setSpentTime(int spentTime) {
        gameInfoPresenter.getDigitalClockPresenter().setTimeInSeconds(spentTime);
    }

    @Override
    public void generateNextColors() {
        gameInfoPresenter.getNextBallsPresenter().generateNextColors();
    }

    @Override
    public void setNextColors(Color[] nextColors) {
        gameInfoPresenter.getNextBallsPresenter().setNextColors(nextColors);
    }

    @Override
    public Color[] getNextColors() {
        return gameInfoPresenter.getNextBallsPresenter().getNextColors();
    }

    @Override
    public void drawGameInfo(Graphics g) {
        gameInfoPresenter.draw(g);
    }
}
