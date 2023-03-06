package thbt.webng.com.game.board;

import thbt.webng.com.game.base.Position;
import thbt.webng.com.game.base.Square;
import thbt.webng.com.game.option.GameOptionsManager;
import thbt.webng.com.game.option.NextBallsDisplayType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameBoardView extends JPanel {
    private final int left = 1;
    private final int top = 53;

    private Square[][] squares;
    private GameBoardViewListener viewListener;

    public GameBoardView(Square[][] squares) {
        initializeSquares(squares);

        setPreferredSize(getBoardSize());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                viewListener.onPlayAt(e.getX(), e.getY());
            }
        });
    }

    public void setViewListener(GameBoardViewListener viewListener) {
        this.viewListener = viewListener;
    }

    public Position getPositionAtMouse(int x, int y) {
        int i = (y - top) / getSquareSize();
        int j = (x - left) / getSquareSize();

        if (y < top || i >= getRowCount() || x < left || j >= getColumnCount()) {
            return null;
        }

        return new Position(i, j);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
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

    private void initializeSquares(Square[][] squares) {
        this.squares = squares;
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                this.squares[i][j] = new Square(this);
                this.squares[i][j].setLeft(left + j * getSquareSize());
                this.squares[i][j].setTop(top + i * getSquareSize());
            }
        }
    }

    /**
     * @return size of the game board including game information displayed on the
     * top
     */
    private Dimension getBoardSize() {
        return new Dimension(2 * left + getColumnCount() * getSquareSize(),
                top + getRowCount() * getSquareSize() + 1);
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
