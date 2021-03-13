package thbt.webng.com.game.option;

public class GameInfo {

	public static GameInfo getInstance() {
		return instance;
	}

	private GameInfo() {
	}

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

	public boolean isSelectedBallSound() {
		return selectedBallSound;
	}

	public void setSelectedBallSound(boolean selectedBallSound) {
		this.selectedBallSound = selectedBallSound;
	}

	public boolean isShapeCompleteSound() {
		return shapeCompleteSound;
	}

	public void setShapeCompleteSound(boolean shapeCompleteSound) {
		this.shapeCompleteSound = shapeCompleteSound;
	}

	public boolean isMovementSound() {
		return movementSound;
	}

	public void setMovementSound(boolean movementSound) {
		this.movementSound = movementSound;
	}

	private static GameInfo instance = new GameInfo();

	private GameType gameType = GameType.Lines;
	private GameType defaultGameType = GameType.Lines;

	private NextBallDisplayType nextBallDisplayType = NextBallDisplayType.ShowBoth;

	private int jumpValue = 60;
	private int explosionValue = 30;
	private int movementValue = 10;
	private int appearanceValue = 20;

	private boolean selectedBallSound = false;
	private boolean shapeCompleteSound = true;
	private boolean movementSound = true;
}
