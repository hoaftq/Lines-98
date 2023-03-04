package thbt.webng.com.game.board;

import thbt.webng.com.game.BallState;
import thbt.webng.com.game.info.GameInfoPresenter;
import thbt.webng.com.game.option.GameOptionsManager;
import thbt.webng.com.game.option.GameType;

import java.awt.*;

public class GameBoardPresenter {

    private final GameBoardModel model;

    private final GameBoardView view;

    public GameBoardPresenter(GameBoardModel model, GameBoardView view) {
        this.model = model;
        this.view = view;
    }

    public void newGame(GameType gameType) {
        model.newGame(gameType);
        view.repaint();
    }

    public void newGame() {
        newGame(GameOptionsManager.getCurrentGameOptions().getDefaultGameType());
    }

    public void playAt(int mouseX, int mouseY) {
        var position = view.squareFromMousePos(mouseX, mouseY);
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

    public Dimension getBoardSize() {
        return view.getBoardSize();
    }

    public void draw(Graphics g) {
        view.draw(g);
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

    public GameInfoPresenter getGameInfoBoard() {
        return view.getGameInfoPresenter();
    }
}
