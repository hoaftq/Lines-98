package thbt.webng.com.game.option;

import javax.swing.*;

public class OptionsDialogPresenter {

    private OptionsDialogView view;
    private GameOptions gameOptions = GameOptionsManager.getCurrentGameOptions().clone();

    public OptionsDialogPresenter(JFrame owner) {
        this.view = new OptionsDialogView(owner, this);
    }

    public OptionsDialogView getView() {
        return view;
    }

    public GameType getDefaultGameType() {
        return gameOptions.getDefaultGameType();
    }

    public void onSetDefaultGameType(GameType gameType) {
        gameOptions.setDefaultGameType(gameType);
    }

    public NextBallsDisplayType getNextBallsDisplayType() {
        return gameOptions.getNextBallsDisplayTypes();
    }

    public void onSetNextBallsDisplayType(NextBallsDisplayType nextBallsDisplayType) {
        gameOptions.setNextBallsDisplayType(nextBallsDisplayType);
    }

    public boolean isDestroySound() {
        return gameOptions.isDestroySound();
    }

    public void onSetDestroySound(boolean isSelected) {
        gameOptions.setDestroySound(isSelected);
    }

    public boolean isMovementSound() {
        return gameOptions.isMovementSound();
    }

    public void onSetMovementSound(boolean isSelected) {
        gameOptions.setMovementSound(isSelected);
    }

    public boolean isBallJumpingSound() {
        return gameOptions.isBallJumpingSound();
    }

    public void onSetBallJumpingSound(boolean isSelected) {
        gameOptions.setBallJumpingSound(isSelected);
    }

    public void onOK() {
        GameOptionsManager.setCurrentGameOptions(gameOptions);
    }

    public void onCancel() {
    }
}
