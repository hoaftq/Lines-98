package thbt.webng.com.game.option;

import java.io.Serializable;

import thbt.webng.com.game.common.StorageUtil;

public class GameInfo implements Cloneable, Serializable {

	private static final long serialVersionUID = -4449406179310549758L;

	public GameType getGameType() {
		return gameType;
	}

	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}

	public GameType getDefaultGameType() {
		return defaultGameType;
	}

	public void setDefaultGameType(GameType defaultGameType) {
		this.defaultGameType = defaultGameType;
	}

	public NextBallDisplayType getNextBallDisplayType() {
		return nextBallDisplayType;
	}

	public void setNextBallDisplayType(NextBallDisplayType nextBallDisplayType) {
		this.nextBallDisplayType = nextBallDisplayType;
	}

	public int getJumpValue() {
		return jumpValue;
	}

	public void setJumpValue(int jumpValue) {
		this.jumpValue = jumpValue;
	}

	public int getExplosionValue() {
		return explosionValue;
	}

	public void setExplosionValue(int explosionValue) {
		this.explosionValue = explosionValue;
	}

	public int getMovementValue() {
		return movementValue;
	}

	public void setMovementValue(int movementValue) {
		this.movementValue = movementValue;
	}

	public int getAppearanceValue() {
		return appearanceValue;
	}

	public void setAppearanceValue(int appearanceValue) {
		this.appearanceValue = appearanceValue;
	}

	public boolean isBallJumpingSound() {
		return ballJumpingSound;
	}

	public void setBallJumpingSound(boolean ballJumpingSound) {
		this.ballJumpingSound = ballJumpingSound;
	}

	public boolean isDestroySound() {
		return destroySound;
	}

	public void setDestroySound(boolean destroySound) {
		this.destroySound = destroySound;
	}

	public boolean isMovementSound() {
		return movementSound;
	}

	public void setMovementSound(boolean movementSound) {
		this.movementSound = movementSound;
	}

	public static GameInfo getCurrentInstance() {
		return currentInstance;
	}

	public static void setCurrentInstance(GameInfo gameInfo) {
		currentInstance = gameInfo;
		StorageUtil.save(currentInstance, CONFIG_FILE_NAME);
	}

	@Override
	protected GameInfo clone() {
		GameInfo gi = new GameInfo();
		gi.setGameType(gameType);
		gi.setDefaultGameType(defaultGameType);
		gi.setNextBallDisplayType(nextBallDisplayType);
		gi.setJumpValue(jumpValue);
		gi.setExplosionValue(explosionValue);
		gi.setMovementValue(movementValue);
		gi.setAppearanceValue(appearanceValue);
		gi.setBallJumpingSound(ballJumpingSound);
		gi.setDestroySound(destroySound);
		gi.setMovementSound(movementSound);
		return gi;
	}

	private GameType gameType = GameType.LINE;
	private GameType defaultGameType = GameType.LINE;

	private NextBallDisplayType nextBallDisplayType = NextBallDisplayType.ShowBoth;

	private int jumpValue = 60;
	private int explosionValue = 30;
	private int movementValue = 10;
	private int appearanceValue = 20;

	private boolean ballJumpingSound = false;
	private boolean destroySound = true;
	private boolean movementSound = true;

	private static GameInfo currentInstance;
	private final static String CONFIG_FILE_NAME = "Config";
	static {
		currentInstance = StorageUtil.<GameInfo>load(CONFIG_FILE_NAME).orElse(new GameInfo());
	}
}
