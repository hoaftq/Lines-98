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

        initializeSquares();
    }

    public void setViewListener(GameBoardViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public void draw(Graphics g) {
        viewListener.drawGameInfo(g);

        var nextBallsDisplayType = GameOptionsManager.getCurrentGameOptions().getNextBallsDisplayType();
        boolean displayGrowingBalls = nextBallsDisplayType == NextBallsDisplayType.ShowBoth
                || nextBallsDisplayType == NextBallsDisplayType.ShowOnField;
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                squares[i][j].draw(g, displayGrowingBalls);
            }
        }
    }

    /**
     * @return size of the game board including game information displayed on the
     * top
     */
    public Dimension getBoardSize() {
        return new Dimension(2 * left + getColumnCount() * getSquareSize(),
                top + getRowCount() * getSquareSize() + 1);
    }

    public void repaint() {
        gamePanel.repaint();
    }

    public Position getPositionAtMouse(int x, int y) {
        int i = (y - top) / getSquareSize();
        int j = (x - left) / getSquareSize();

        if (y < top || i >= getRowCount() || x < left || j >= getColumnCount()) {
            return null;
        }

        return new Position(i, j);
    }

    private void initializeSquares() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                this.squares[i][j] = new Square(this.gamePanel);
                this.squares[i][j].setLeft(left + j * getSquareSize());
                this.squares[i][j].setTop(top + i * getSquareSize());
            }
        }
    }

    private int getSquareSize() {
        return squares[0][0].getSize();
    }

    private int getRowCount() {
        return squares.length;
    }

    private int getColumnCount() {
        return squares[0].length;
    }
}
