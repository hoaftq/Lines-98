package thbt.webng.com.game.option;

import java.io.Serial;
import java.io.Serializable;

public class GameOptions implements Cloneable, Serializable {
    @Serial
    private static final long serialVersionUID = -4449406179310549758L;

    private GameType gameType = GameType.LINE;
    private GameType defaultGameType = GameType.LINE;
    private NextBallsDisplayType nextBallsDisplayType = NextBallsDisplayType.ShowBoth;
    private int jumpingStepDelay = 30;
    private int explosionStepDelay = 15;
    private int movementStepDelay = 10;
    private int appearanceStepDelay = 10;
    private boolean playJumpSound = false;
    private boolean playDestroySound = true;
    private boolean playMoveSound = true;

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

    public int getJumpingStepDelay() {
        return jumpingStepDelay;
    }

    public GameOptions setJumpingStepDelay(int jumpingStepDelay) {
        this.jumpingStepDelay = jumpingStepDelay;
        return this;
    }

    public int getExplosionStepDelay() {
        return explosionStepDelay;
    }

    public GameOptions setExplosionStepDelay(int explosionStepDelay) {
        this.explosionStepDelay = explosionStepDelay;
        return this;
    }

    public int getMovementStepDelay() {
        return movementStepDelay;
    }

    public GameOptions setMovementStepDelay(int movementStepDelay) {
        this.movementStepDelay = movementStepDelay;
        return this;
    }

    public int getAppearanceStepDelay() {
        return appearanceStepDelay;
    }

    public GameOptions setAppearanceStepDelay(int appearanceStepDelay) {
        this.appearanceStepDelay = appearanceStepDelay;
        return this;
    }

    public boolean isPlayJumpSound() {
        return playJumpSound;
    }

    public GameOptions setPlayJumpSound(boolean playJumpSound) {
        this.playJumpSound = playJumpSound;
        return this;
    }

    public boolean isPlayDestroySound() {
        return playDestroySound;
    }

    public GameOptions setPlayDestroySound(boolean playDestroySound) {
        this.playDestroySound = playDestroySound;
        return this;
    }

    public boolean isPlayMoveSound() {
        return playMoveSound;
    }

    public GameOptions setPlayMoveSound(boolean playMoveSound) {
        this.playMoveSound = playMoveSound;
        return this;
    }

    @Override
    protected GameOptions clone() {
        return new GameOptions()
                .setGameType(gameType)
                .setDefaultGameType(defaultGameType)
                .setNextBallsDisplayType(nextBallsDisplayType)
                .setJumpingStepDelay(jumpingStepDelay)
                .setExplosionStepDelay(explosionStepDelay)
                .setMovementStepDelay(movementStepDelay)
                .setAppearanceStepDelay(appearanceStepDelay)
                .setPlayJumpSound(playJumpSound)
                .setPlayDestroySound(playDestroySound)
                .setPlayMoveSound(playMoveSound);
    }
}
