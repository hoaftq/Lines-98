package thbt.webng.com.game.base;

import thbt.webng.com.game.option.GameOptionsManager;
import thbt.webng.com.game.sound.SoundManager;

import java.awt.*;

public class Ball implements Cloneable {
    private static final int MATURITY_SIZE = 33;
    private static final int GROWING_SIZE = 9;

    private final NoninteractiveBall noninteractiveBall;

    protected Square square;
    private BallState ballState;
    private boolean isMovingUp = true;
    private Thread animateThread;

    public Ball(Color color, BallState ballState, Square square) {
        noninteractiveBall = new NoninteractiveBall();
        noninteractiveBall.setColor(color);

        this.square = square;
        setBallState(ballState);
    }

    public Color getColor() {
        return noninteractiveBall.getColor();
    }

    public void setSquare(Square square) {
        this.square = square;
        setSize(getSize());
    }

    public BallState getBallState() {
        return ballState;
    }

    public void setBallState(BallState ballState) {
        this.ballState = ballState;

        if (ballState == BallState.GROWING) {
            setSize(GROWING_SIZE);
        } else if (ballState == BallState.MATURE) {
            setSize(MATURITY_SIZE);
        } else if (ballState == BallState.ANIMATE) {
            setSize(MATURITY_SIZE);
            select();
        }
    }

    public void grow() {
        if (ballState != BallState.GROWING) {
            throw new IllegalStateException();
        }

        var size = noninteractiveBall.getWidth();
        if (size < MATURITY_SIZE) {
            setSize(size + 1);
            square.repaint();
            return;
        }

        ballState = BallState.MATURE;
    }

    public void shrink() {
        if (ballState != BallState.MATURE) {
            throw new IllegalStateException();
        }

        var size = noninteractiveBall.getWidth();
        if (size > 0) {
            setSize(size - 1);
            square.repaint();
            return;
        }

        ballState = BallState.REMOVED;
    }

    public void select() {
        // TODO need this
        if (ballState == BallState.GROWING || ballState == BallState.REMOVED) {
            throw new IllegalStateException();
        }

        if (ballState == BallState.ANIMATE) {
            unSelect();
        }

        ballState = BallState.ANIMATE;
        animateJumping();
    }

    public void unSelect() {
        animateThread.interrupt();

        ballState = BallState.MATURE;
        noninteractiveBall.setTop(square.getTop() + (square.getSize() - MATURITY_SIZE) / 2);
        isMovingUp = true;
        square.repaint();
    }

    public void draw(Graphics g) {
        noninteractiveBall.draw(g);
    }

    @Override
    public Ball clone() {
        return new Ball(noninteractiveBall.getColor(),
                ballState == BallState.ANIMATE ? BallState.MATURE : ballState,
                square);
    }

    private int getSize() {
        return noninteractiveBall.getWidth();
    }

    private void setSize(int size) {
        int padding = (square.getSize() - size) / 2;
        noninteractiveBall.setLeft(square.getLeft() + padding);
        noninteractiveBall.setTop(square.getTop() + padding);
        noninteractiveBall.setWidth(size);
        noninteractiveBall.setHeight(size);
    }

    private void animateJumping() {
        animateThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (isMovingUp) {
                    if (noninteractiveBall.getTop() - square.getTop() > 2) {
                        noninteractiveBall.setTop(noninteractiveBall.getTop() - 1);
                    } else {
                        isMovingUp = false;
                    }
                } else {
                    if (noninteractiveBall.getTop() + noninteractiveBall.getHeight() - square.getTop() < square.getSize() - 2) {
                        noninteractiveBall.setTop(noninteractiveBall.getTop() + 1);
                    } else {
                        isMovingUp = true;
                        if (GameOptionsManager.getCurrentGameOptions().isPlayJumpSound()) {
                            SoundManager.playJumSound();
                        }
                    }
                }

                square.repaint();

                try {
                    Thread.sleep(GameOptionsManager.getCurrentGameOptions().getJumpingStepDelay());
                } catch (InterruptedException e) {
                    System.out.println("Interrupted by unSelect");
                    break;
                }
            }
        });
        animateThread.start();
    }
}
