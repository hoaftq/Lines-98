package thbt.webng.com.game;

import thbt.webng.com.AboutDialog;
import thbt.webng.com.game.board.GameBoardModel;
import thbt.webng.com.game.board.GameBoardPresenter;
import thbt.webng.com.game.board.GameBoardView;
import thbt.webng.com.game.info.GameInfoPresenter;
import thbt.webng.com.game.option.GameType;
import thbt.webng.com.game.option.OptionsDialogPresenter;
import thbt.webng.com.game.scorehistory.HighScoreDialogPresenter;
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
                presenter.endGame();
            }
        });
    }

    private void createGameBoardObjects() {
        var model = new GameBoardModel();
        var view = new GameBoardView(model.getSquares());
        var gameInfoPresenter = new GameInfoPresenter(view);
        presenter = new GameBoardPresenter(model, view, gameInfoPresenter, (highScore) -> showHighScoreDialog());
        add(view);
    }

    private void addGameMenu() {
        var gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');

        var newMenuItem = new JMenuItem("New", 'N');
        gameMenu.add(newMenuItem);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
        newMenuItem.addActionListener((e) -> {
            presenter.endGame();
            presenter.newGame();
        });

        var newLinesMenuItem = new JMenuItem("New Lines Game", 'L');
        gameMenu.add(newLinesMenuItem);
        newLinesMenuItem.addActionListener((e) -> {
            presenter.endGame();
            presenter.newGame(GameType.LINE);
        });

        var newSquaresMenuItem = new JMenuItem("New Squares Game", 'S');
        gameMenu.add(newSquaresMenuItem);
        newSquaresMenuItem.addActionListener((e) -> {
            presenter.endGame();
            presenter.newGame(GameType.SQUARE);
        });

        var newBlocksMenuItem = new JMenuItem("New Blocks Game", 'B');
        gameMenu.add(newBlocksMenuItem);
        newBlocksMenuItem.addActionListener((e) -> {
            presenter.endGame();
            presenter.newGame(GameType.BLOCK);
        });

        gameMenu.addSeparator();

        var optionsMenuItem = new JMenuItem("Options", 'O');
        gameMenu.add(optionsMenuItem);
        optionsMenuItem.addActionListener((e) -> {
            showOptionsDialog();
            presenter.repaint();
        });

        gameMenu.addSeparator();

        var saveGameMenuItem = new JMenuItem("High Scores", 'H');
        gameMenu.add(saveGameMenuItem);
        saveGameMenuItem.addActionListener((e) -> showHighScoreDialog());

        getJMenuBar().add(gameMenu);
    }

    private void addControlMenu() {
        var controlMenu = new JMenu("Control");
        controlMenu.setMnemonic('C');

        var saveGameMenuItem = new JMenuItem("Save Game", 'S');
        controlMenu.add(saveGameMenuItem);
        saveGameMenuItem.addActionListener((e) -> presenter.saveGame());

        var loadGameMenuItem = new JMenuItem("Load Game", 'L');
        controlMenu.add(loadGameMenuItem);
        loadGameMenuItem.addActionListener((e) -> presenter.loadGame());

        var endGameMenuItem = new JMenuItem("End Game", 'E');
        controlMenu.add(endGameMenuItem);
        endGameMenuItem.addActionListener((e) -> {
            presenter.endGame();
            presenter.newGame();
        });

        controlMenu.addSeparator();

        var stepBackMenuItem = new JMenuItem("Step Back");
        stepBackMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        controlMenu.add(stepBackMenuItem);
        stepBackMenuItem.addActionListener((e) -> presenter.stepBack());

        getJMenuBar().add(controlMenu);
    }

    private void addHelpMenu() {
        var helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');

        var aboutMenuItem = new JMenuItem("About", 'A');
        helpMenu.add(aboutMenuItem);
        aboutMenuItem.addActionListener((e) -> showAboutDialog());

        getJMenuBar().add(helpMenu);
    }

    private void showOptionsDialog() {
        var optionsDialogPresenter = new OptionsDialogPresenter(this);
        optionsDialogPresenter.show();
    }

    private void showHighScoreDialog() {
        var highScoreDialogPresenter = new HighScoreDialogPresenter(GameFrame.this);
        highScoreDialogPresenter.show();
    }

    private void showAboutDialog() {
        var aboutDialog = new AboutDialog(GameFrame.this);
        aboutDialog.setVisible(true);
    }
}
