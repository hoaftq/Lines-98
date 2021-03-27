package thbt.webng.com.game;

import java.awt.Color;
import java.util.List;

import thbt.webng.com.game.common.PrimitiveBall;
import thbt.webng.com.game.option.GameInfo;
import thbt.webng.com.sound.SoundManager;

public class Ball extends PrimitiveBall {

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

	public BallState getBallState() {
		return ballState;
	}

	public void setBallState(BallState ballState) {
		this.ballState = ballState;

		if (ballState == BallState.Growing) {
			setSize(GROWING_SIZE);
		} else if (ballState == BallState.Maturity) {
			setSize(MATURITY_SIZE);
		} else if (ballState == BallState.Animate) {
			setSize(MATURITY_SIZE);
			select();
		}
	}

	public void select() {
		if (ballState == BallState.Growing || ballState == BallState.Removed) {
			throw new IllegalStateException();
		}

		if (ballState == BallState.Animate) {
			unSelect();
		}

		ballState = BallState.Animate;

		animateThread = new Thread(() -> {
			while (ballState == BallState.Animate) {
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
						if (GameInfo.getCurrentInstance().isBallJumpingSound()) {
							SoundManager.playJumSound();
						}
					}
				}

				square.repaint();

				try {
					Thread.sleep(GameInfo.getCurrentInstance().getJumpValue());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		animateThread.start();
	}

	public void unSelect() {
		ballState = BallState.Maturity;

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

	public static void growBall(final List<Square> squareList) {
		for (Square square : squareList) {
			if (square.getBallState() != BallState.Growing) {
				throw new IllegalStateException();
			}

			square.getBall().ballState = BallState.Maturity;
		}

		while (squareList.get(0).getBall().width < MATURITY_SIZE) {
			for (Square square : squareList) {
				Ball ball = square.getBall();
				ball.setSize(ball.width + 2);
				square.repaint();
			}

			try {
				Thread.sleep(GameInfo.getCurrentInstance().getAppearanceValue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void hideBall(final List<Square> squareList) {
		for (Square square : squareList) {
			if (square.getBallState() != BallState.Maturity && square.getBallState() != BallState.Animate) {
				throw new IllegalStateException();
			}

			square.getBall().ballState = BallState.Removed;
		}

		while (squareList.get(0).getBall().width > GROWING_SIZE) {
			for (Square square : squareList) {
				Ball ball = square.getBall();
				ball.setSize(ball.width - 2);
				square.repaint();
			}

			try {
				Thread.sleep(GameInfo.getCurrentInstance().getExplosionValue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (Square square : squareList) {
			square.setBall(null);
			square.repaint();
		}

		if (GameInfo.getCurrentInstance().isDestroySound()) {
			SoundManager.playDestroySound();
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new Ball(color, ballState == BallState.Animate ? BallState.Maturity : ballState, square);
	}

	protected Square square;
	private BallState ballState;
	private boolean isUpDirect = true;
	private Thread animateThread;

	public static final int MATURITY_SIZE = 33;
	public static final int GROWING_SIZE = 9;
}
