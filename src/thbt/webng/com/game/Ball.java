package thbt.webng.com.game;

import thbt.webng.com.game.option.GameOptions;
import thbt.webng.com.game.option.GameOptionsManager;
import thbt.webng.com.game.sound.SoundManager;
import thbt.webng.com.game.util.PrimitiveBall;

import java.awt.*;
import java.util.List;

public class Ball extends PrimitiveBall {

    public static final int MATURITY_SIZE = 33;
    public static final int GROWING_SIZE = 9;
    protected Square square;
    private BallState ballState;
    private boolean isUpDirect = true;
    private Thread animateThread;

    public Ball(Color color, BallState ballState, Square square) {
        super();
        this.square = square;
        setBallState(ballState);
        setColor(color);
    }

    public static void growBall(final List<Square> squareList) {
        for (Square square : squareList) {
            if (square.getBallState() != BallState.GROWING) {
                throw new IllegalStateException();
            }

            square.getBall().ballState = BallState.MATURE;
        }

        while (squareList.get(0).getBall().width < MATURITY_SIZE) {
            for (Square square : squareList) {
                Ball ball = square.getBall();
                ball.setSize(ball.width + 2);
                square.repaint();
            }

            try {
                Thread.sleep(GameOptionsManager.getCurrentGameOptions().getAppearanceValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void hideBall(final List<Square> squareList) {
        for (Square square : squareList) {
            if (square.getBallState() != BallState.MATURE && square.getBallState() != BallState.ANIMATE) {
                throw new IllegalStateException();
            }

            square.getBall().ballState = BallState.REMOVED;
        }

        while (squareList.get(0).getBall().width > GROWING_SIZE) {
            for (Square square : squareList) {
                Ball ball = square.getBall();
                ball.setSize(ball.width - 2);
                square.repaint();
            }

            try {
                Thread.sleep(GameOptionsManager.getCurrentGameOptions().getExplosionValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Square square : squareList) {
            square.setBall(null);
            square.repaint();
        }

        if (GameOptionsManager.getCurrentGameOptions().isDestroySound()) {
            SoundManager.playDestroySound();
        }
    }

    @Override
    public int getLeft() {
        return square.getLeft() + left;
    }

    @Override
    public int getTop() {
        return square.getTop() + top;
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

    public void select() {
        if (ballState == BallState.GROWING || ballState == BallState.REMOVED) {
            throw new IllegalStateException();
        }

        if (ballState == BallState.ANIMATE) {
            unSelect();
        }

        ballState = BallState.ANIMATE;

        animateThread = new Thread(() -> {
            while (ballState == BallState.ANIMATE) {
                if (isUpDirect) {
                    if (top > 2) {
                        top -= 2;
                    } else {
                        isUpDirect = !isUpDirect;
                    }
                } else {
                    if (top + height < square.getSize() - 2) {
                        top += 2;
                    } else {
                        isUpDirect = !isUpDirect;
                        if (GameOptionsManager.getCurrentGameOptions().isBallJumpingSound()) {
                            SoundManager.playJumSound();
                        }
                    }
                }

                square.repaint();

                try {
                    Thread.sleep(GameOptionsManager.getCurrentGameOptions().getJumpValue());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        animateThread.start();
    }

    public void unSelect() {
        ballState = BallState.MATURE;

        // animateThread.stop();
        try {
            animateThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        top = (square.getSize() - MATURITY_SIZE) / 2;
        isUpDirect = true;
        square.repaint();
    }

    private void setSize(int size) {
        width = height = size;
        left = top = (square.getSize() - size) / 2;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Ball(color, ballState == BallState.ANIMATE ? BallState.MATURE : ballState, square);
    }
}
