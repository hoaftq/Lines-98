package thbt.webng.com.game.option;

import javax.swing.*;

public class OptionsDialogPresenter {

    private final OptionsDialogView view;
    private final GameOptions gameOptions = GameOptionsManager.getCurrentGameOptions().clone();

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
        return gameOptions.isPlayDestroySound();
    }

    public void onSetDestroySound(boolean isSelected) {
        gameOptions.setPlayDestroySound(isSelected);
    }

    public boolean isMovementSound() {
        return gameOptions.isPlayMoveSound();
    }

    public void onSetMovementSound(boolean isSelected) {
        gameOptions.setPlayMoveSound(isSelected);
    }

    public boolean isBallJumpingSound() {
        return gameOptions.isPlayJumpSound();
    }

    public void onSetBallJumpingSound(boolean isSelected) {
        gameOptions.setPlayJumpSound(isSelected);
    }

    public void show() {
        view.setVisible(true);
    }

    public void onOK() {
        GameOptionsManager.setCurrentGameOptions(gameOptions);
        view.dispose();
    }

    public void onCancel() {
        view.dispose();
    }
}
