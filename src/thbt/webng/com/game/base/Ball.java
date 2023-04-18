package thbt.webng.com.game.base;

import thbt.webng.com.game.option.GameOptionsManager;
import thbt.webng.com.game.sound.SoundManager;

import java.awt.*;

public class Ball extends BaseBall {
    private static final int MATURITY_SIZE = 33;
    private static final int GROWING_SIZE = 9;

    protected Square square;
    private BallState ballState;
    private boolean isMovingUp = true;
    private Thread animateThread;

    public Ball(Color color, BallState ballState, Square square) {
        super();
        this.square = square;
        setBallState(ballState);
        setColor(color);
    }

    @Override
    public int getLeft() {
        return square.getLeft() + left;
    }

    @Override
    public int getTop() {
        return square.getTop() + top;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
        left = (square.getSize() - width) / 2;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
        top = (square.getSize() - height) / 2;
    }

    public int getSize() {
        return width;
    }

    private void setSize(int size) {
        setWidth(size);
        setHeight(size);
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

        var size = getSize();
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

        var size = getSize();
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
        top = (square.getSize() - MATURITY_SIZE) / 2;
        isMovingUp = true;
        square.repaint();
    }

    @Override
    public Ball clone() {
        return new Ball(color, ballState == BallState.ANIMATE ? BallState.MATURE : ballState, square);
    }

    private void animateJumping() {
        animateThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                if (isMovingUp) {
                    if (top > 2) {
                        top -= 1;
                    } else {
                        isMovingUp = false;
                    }
                } else {
                    if (top + height < square.getSize() - 2) {
                        top += 1;
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
