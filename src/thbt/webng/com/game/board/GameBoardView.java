package thbt.webng.com.game.board;

import thbt.webng.com.game.GamePanel;
import thbt.webng.com.game.Position;
import thbt.webng.com.game.Square;
import thbt.webng.com.game.option.GameOptionsManager;
import thbt.webng.com.game.option.NextBallsDisplayType;

import java.awt.*;

public class GameBoardView {
    private final int left = 1;
    private final int top = 53;

    private final Square[][] squares;
    private final GamePanel gamePanel;

    private GameBoardViewListener viewListener;

    public GameBoardView(GamePanel gamePanel, Square[][] squares) {
        this.gamePanel = gamePanel;
        this.squares = squares;

        for (int i = 0; i < this.squares.length; i++) {
            for (int j = 0; j < this.squares[0].length; j++) {
                this.squares[i][j] = new Square(this.gamePanel); // TODO is it model responsibility
                this.squares[i][j].setLeft(left + j * Square.DEFAULT_SIZE);
                this.squares[i][j].setTop(top + i * Square.DEFAULT_SIZE);
            }
        }
    }

    public void setViewListener(GameBoardViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public void draw(Graphics g) {
        viewListener.drawGameInfo(g);

        boolean displayGrowingBalls = GameOptionsManager.getCurrentGameOptions()
                .getNextBallsDisplayTypes() == NextBallsDisplayType.ShowBoth
                || GameOptionsManager.getCurrentGameOptions().getNextBallsDisplayTypes() == NextBallsDisplayType.ShowOnField;
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[0].length; j++) {
                squares[i][j].draw(g, displayGrowingBalls);
            }
        }
    }

    /**
     * @return size of the game board including game information displayed on the
     * top
     */
    public Dimension getBoardSize() {
        return new Dimension(2 * left + squares[0].length * Square.DEFAULT_SIZE,
                top + squares.length * Square.DEFAULT_SIZE + 1);
    }

    public void repaint() {
        gamePanel.repaint();
    }

    public Position squareFromMousePos(int x, int y) {
        int i = (y - top) / Square.DEFAULT_SIZE;
        int j = (x - left) / Square.DEFAULT_SIZE;

        if (y < top || i >= squares.length || x < left || j >= squares[0].length) {
            return null;
        }

        return new Position(i, j);
    }
}
