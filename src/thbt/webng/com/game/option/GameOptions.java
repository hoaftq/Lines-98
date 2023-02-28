package thbt.webng.com.game.option;

import java.io.Serializable;

public class GameOptions implements Cloneable, Serializable {
    private static final long serialVersionUID = -4449406179310549758L;

    private GameTypes gameTypes = GameTypes.LINE;
    private GameTypes defaultGameTypes = GameTypes.LINE;
    private NextBallsDisplayTypes nextBallsDisplayTypes = NextBallsDisplayTypes.ShowBoth;
    private int jumpValue = 60;
    private int explosionValue = 30;
    private int movementValue = 10;
    private int appearanceValue = 20;
    private boolean ballJumpingSound = false;
    private boolean destroySound = true;
    private boolean movementSound = true;

    public GameTypes getGameType() {
        return gameTypes;
    }

    public GameOptions setGameType(GameTypes gameTypes) {
        this.gameTypes = gameTypes;
        return this;
    }

    public GameTypes getDefaultGameType() {
        return defaultGameTypes;
    }

    public GameOptions setDefaultGameType(GameTypes defaultGameTypes) {
        this.defaultGameTypes = defaultGameTypes;
        return this;
    }

    public NextBallsDisplayTypes getNextBallDisplayType() {
        return nextBallsDisplayTypes;
    }

    public GameOptions setNextBallDisplayType(NextBallsDisplayTypes nextBallsDisplayTypes) {
        this.nextBallsDisplayTypes = nextBallsDisplayTypes;
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
                .setGameType(gameTypes)
                .setDefaultGameType(defaultGameTypes)
                .setNextBallDisplayType(nextBallsDisplayTypes)
                .setJumpValue(jumpValue)
                .setExplosionValue(explosionValue)
                .setMovementValue(movementValue)
                .setAppearanceValue(appearanceValue)
                .setBallJumpingSound(ballJumpingSound)
                .setDestroySound(destroySound)
                .setMovementSound(movementSound);
    }
}
