package thbt.webng.com.game;

import thbt.webng.com.AboutDialog;
import thbt.webng.com.game.board.GameBoardModel;
import thbt.webng.com.game.board.GameBoardPresenter;
import thbt.webng.com.game.board.GameBoardView;
import thbt.webng.com.game.info.GameInfoPresenter;
import thbt.webng.com.game.option.GameType;
import thbt.webng.com.game.option.OptionsDialogPresenter;
import thbt.webng.com.game.scorehistory.HighScoreDialogPresenter;
import thbt.webng.com.game.scorehistory.PlayerScore;
import thbt.webng.com.game.scorehistory.PlayerScoreHistory;
import thbt.webng.com.game.util.WindowUtil;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serial;

public class GameFrame extends JFrame {

    @Serial
    private static final long serialVersionUID = -8199515970527728642L;

    private GameBoardPresenter presenter;

    public GameFrame() {
        setTitle("Lines 98");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setJMenuBar(new JMenuBar());
        addGameMenu();
        addControlMenu();
        addHelpMenu();

        createGameBoardObjects();

        pack();
        setResizable(false);

        WindowUtil.centerOwner(this);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                saveHighScore();
            }
        });
    }

    private void createGameBoardObjects() {
        var model = new GameBoardModel();
        var view = new GameBoardView(model.getSquares());
        var gameInfoPresenter = new GameInfoPresenter(view);
        presenter = new GameBoardPresenter(model, view, gameInfoPresenter);
        add(view);
    }

    private void addGameMenu() {
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');

        JMenuItem newMenuItem;
        gameMenu.add(newMenuItem = new JMenuItem("New", 'N'));
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        newMenuItem.addActionListener((e) -> {
            saveHighScore();
            presenter.newGame();
        });

        JMenuItem newLinesMenuItem = new JMenuItem("New Lines Game", 'L');
        gameMenu.add(newLinesMenuItem);
        newLinesMenuItem.addActionListener((e) -> {
            saveHighScore();
            presenter.newGame(GameType.LINE);
        });

        JMenuItem newSquaresMenuItem = new JMenuItem("New Squares Game", 'S');
        gameMenu.add(newSquaresMenuItem);
        newSquaresMenuItem.addActionListener((e) -> {
            saveHighScore();
            presenter.newGame(GameType.SQUARE);
        });

        JMenuItem newBlocksMenuItem = new JMenuItem("New Blocks Game", 'B');
        gameMenu.add(newBlocksMenuItem);
        newBlocksMenuItem.addActionListener((e) -> {
            saveHighScore();
            presenter.newGame(GameType.BLOCK);
        });

        gameMenu.addSeparator();

        JMenuItem optionsMenuItem = new JMenuItem("Options", 'O');
        gameMenu.add(optionsMenuItem);
        optionsMenuItem.addActionListener((e) -> {
            var optionsDialogPresenter = new OptionsDialogPresenter(this);
            optionsDialogPresenter.show();
            presenter.repaint();
        });

        gameMenu.addSeparator();

        JMenuItem saveGameMenuItem = new JMenuItem("High Scores", 'H');
        gameMenu.add(saveGameMenuItem);
        saveGameMenuItem.addActionListener((e) -> {
            showHighScoreDialog();
        });

        getJMenuBar().add(gameMenu);
    }

    private void addControlMenu() {
        JMenu controlMenu = new JMenu("Control");
        controlMenu.setMnemonic('C');

        JMenuItem saveGameMenuItem = new JMenuItem("Save Game", 'S');
        controlMenu.add(saveGameMenuItem);
        saveGameMenuItem.addActionListener((e) -> {
            presenter.saveGame();
        });

        JMenuItem loadGameMenuItem = new JMenuItem("Load Game", 'L');
        controlMenu.add(loadGameMenuItem);
        loadGameMenuItem.addActionListener((e) -> {
            presenter.loadGame();
        });

        JMenuItem endGameMenuItem = new JMenuItem("End Game", 'E');
        controlMenu.add(endGameMenuItem);
        endGameMenuItem.addActionListener((e) -> {
            saveHighScore();
            presenter.newGame();
        });

        controlMenu.addSeparator();

        JMenuItem stepBackMenuItem = new JMenuItem("Step Back");
        stepBackMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        controlMenu.add(stepBackMenuItem);
        stepBackMenuItem.addActionListener((e) -> {
            presenter.stepBack();
        });

        getJMenuBar().add(controlMenu);
    }

    private void addHelpMenu() {
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        JMenuItem aboutMenuItem = new JMenuItem("About", 'A');
        helpMenu.add(aboutMenuItem);
        aboutMenuItem.addActionListener((e) -> {
            AboutDialog aboutDialog = new AboutDialog(GameFrame.this);
            aboutDialog.setVisible(true);

        });

        getJMenuBar().add(helpMenu);
    }

    private void saveHighScore() {
        GameInfoPresenter gameInfoBoard = presenter.getGameInfoBoard();

        // Stop the playing clock
        gameInfoBoard.getDigitalClockPresenter().stop();

        PlayerScoreHistory playerScoreHistory = PlayerScoreHistory.getInstance();

        // Player gets a new high score
        if (playerScoreHistory.isNewRecord(gameInfoBoard.getScorePresenter().getScore())) {
            String playerName = JOptionPane.showInputDialog(GameFrame.this,
                    "You've got a high score. Please input your name", "New high score", JOptionPane.QUESTION_MESSAGE);
            if (playerName != null && !"".equals(playerName)) {
                // Add a new record to high score history
                playerScoreHistory.addHighScore(new PlayerScore(playerName, gameInfoBoard.getScorePresenter().getScore(),
                        gameInfoBoard.getDigitalClockPresenter().toString()));
                playerScoreHistory.save();

                showHighScoreDialog();
            }
        }

        // Update highest score on the game status board
        gameInfoBoard.getHighestScorePresenter().setScore(playerScoreHistory.getHighestScore());
    }

    private void showHighScoreDialog() {
        HighScoreDialogPresenter highScoreDialogPresenter = new HighScoreDialogPresenter(GameFrame.this);
        highScoreDialogPresenter.show();
    }
}
