package thbt.webng.com.game.scorehistory;

import javax.swing.*;
import java.util.List;

public class HighScoreDialogPresenter {
    private final HighScoreDialogView view;

    public HighScoreDialogPresenter(JFrame owner) {
        view = new HighScoreDialogView(owner, this);
    }

    public HighScoreDialogView getView() {
        return view;
    }

    public List<PlayerScore> getTopScores() {
        return PlayerScoreHistory.getInstance().getTopScores();
    }

    public void show() {
        view.setVisible(true);
    }

    public void onOkButton() {
        view.dispose();
    }
}
