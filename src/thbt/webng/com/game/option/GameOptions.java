package thbt.webng.com.game.option;

import java.io.Serializable;

public class GameOptions implements Cloneable, Serializable {
    private static final long serialVersionUID = -4449406179310549758L;

    private GameType gameType = GameType.LINE;
    private GameType defaultGameType = GameType.LINE;
    private NextBallsDisplayType nextBallsDisplayType = NextBallsDisplayType.ShowBoth;
    private int jumpValue = 60;
    private int explosionValue = 30;
    private int movementValue = 10;
    private int appearanceValue = 20;
    private boolean ballJumpingSound = false;
    private boolean destroySound = true;
    private boolean movementSound = true;

    public GameType getGameType() {
        return gameType;
    }

    public GameOptions setGameType(GameType gameType) {
        this.gameType = gameType;
        return this;
    }

    public GameType getDefaultGameType() {
        return defaultGameType;
    }

    public GameOptions setDefaultGameType(GameType defaultGameType) {
        this.defaultGameType = defaultGameType;
        return this;
    }

    public NextBallsDisplayType getNextBallsDisplayTypes() {
        return nextBallsDisplayType;
    }

    public GameOptions setNextBallsDisplayType(NextBallsDisplayType nextBallsDisplayType) {
        this.nextBallsDisplayType = nextBallsDisplayType;
        return this;
    }

    public int getJumpValue() {
        return jumpValue;
    }

    public GameOptions setJumpValue(int jumpValue) {
        this.jumpValue = jumpValue;
        return this;
    }

    public int getExplosionValue() {
        return explosionValue;
    }

    public GameOptions setExplosionValue(int explosionValue) {
        this.explosionValue = explosionValue;
        return this;
    }

    public int getMovementValue() {
        return movementValue;
    }

    public GameOptions setMovementValue(int movementValue) {
        this.movementValue = movementValue;
        return this;
    }

    public int getAppearanceValue() {
        return appearanceValue;
    }

    public GameOptions setAppearanceValue(int appearanceValue) {
        this.appearanceValue = appearanceValue;
        return this;
    }

    public boolean isBallJumpingSound() {
        return ballJumpingSound;
    }

    public GameOptions setBallJumpingSound(boolean ballJumpingSound) {
        this.ballJumpingSound = ballJumpingSound;
        return this;
    }

    public boolean isDestroySound() {
        return destroySound;
    }

    public GameOptions setDestroySound(boolean destroySound) {
        this.destroySound = destroySound;
        return this;
    }

    public boolean isMovementSound() {
        return movementSound;
    }

    public GameOptions setMovementSound(boolean movementSound) {
        this.movementSound = movementSound;
        return this;
    }

    @Override
    protected GameOptions clone() {
        return new GameOptions()
                .setGameType(gameType)
                .setDefaultGameType(defaultGameType)
                .setNextBallsDisplayType(nextBallsDisplayType)
                .setJumpValue(jumpValue)
                .setExplosionValue(explosionValue)
                .setMovementValue(movementValue)
                .setAppearanceValue(appearanceValue)
                .setBallJumpingSound(ballJumpingSound)
                .setDestroySound(destroySound)
                .setMovementSound(movementSound);
    }
}
