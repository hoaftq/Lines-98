package thbt.webng.com;

import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import thbt.webng.com.game.GameBoard;
import thbt.webng.com.game.GamePanel;
import thbt.webng.com.game.common.WindowUtil;
import thbt.webng.com.game.option.GameInfo;
import thbt.webng.com.game.option.GameType;
import thbt.webng.com.game.option.OptionDialog;
import thbt.webng.com.game.status.GameInfoBoard;
import thbt.webng.com.game.status.PlayerScore;
import thbt.webng.com.game.status.PlayerScoreHistory;

public class GameFrame extends JFrame {

	public GameFrame() {
		setTitle("Lines");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		gamePanel = new GamePanel(this);
		add(gamePanel);

		gameBoard = new GameBoard(gamePanel);
		gamePanel.setGameBoard(gameBoard);

		setJMenuBar(new JMenuBar());
		addGameMenu();
		addHelpMenu();
		addHighScoreMenu();

		pack();
		Dimension frameSize = getSize();
		Dimension boardSize = gameBoard.getBoardSize();
		setSize(boardSize.width, frameSize.height + boardSize.height - 2);
		setResizable(false);

		WindowUtil.centerOwner(this);
	}

	private void addGameMenu() {
		JMenu gameMenu = new JMenu("Game");

		JMenuItem newMenuItem;
		gameMenu.add(newMenuItem = new JMenuItem("New", 'N'));
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		newMenuItem.addActionListener((e) -> {
			saveHighScore();
			GameInfo.getCurrentInstance().setGameType(GameInfo.getCurrentInstance().getDefaultGameType());
			gameBoard.newGame();
		});

		gameMenu.addSeparator();

		JMenuItem newLinesMenuItem = new JMenuItem("New Lines Game", 'L');
		gameMenu.add(newLinesMenuItem);
		newLinesMenuItem.addActionListener((e) -> {
			saveHighScore();
			GameInfo.getCurrentInstance().setGameType(GameType.LINE);
			gameBoard.newGame();
		});

		JMenuItem newSquaresMenuItem = new JMenuItem("New Squares Game", 'S');
		gameMenu.add(newSquaresMenuItem);
		newSquaresMenuItem.addActionListener((e) -> {
			saveHighScore();
			GameInfo.getCurrentInstance().setGameType(GameType.SQUARE);
			gameBoard.newGame();
		});

		JMenuItem newBlocksMenuItem = new JMenuItem("New Blocks Game", 'B');
		gameMenu.add(newBlocksMenuItem);
		newBlocksMenuItem.addActionListener((e) -> {
			saveHighScore();
			GameInfo.getCurrentInstance().setGameType(GameType.BLOCK);
			gameBoard.newGame();
		});

		gameMenu.addSeparator();

		JMenuItem optionsMenuItem = new JMenuItem("Options", 'O');
		gameMenu.add(optionsMenuItem);
		optionsMenuItem.addActionListener((e) -> {
			OptionDialog optionDialog = new OptionDialog(this);
			optionDialog.setVisible(true);
		});

		gameMenu.addSeparator();

		JMenuItem saveGameMenuItem = new JMenuItem("Save Game", 'S');
		gameMenu.add(saveGameMenuItem);
		saveGameMenuItem.addActionListener((e) -> {
			gameBoard.saveGame();
		});

		JMenuItem loadGameMenuItem = new JMenuItem("Load Game", 'L');
		gameMenu.add(loadGameMenuItem);
		loadGameMenuItem.addActionListener((e) -> {
			gameBoard.loadGame();
		});

		JMenuItem endGameMenuItem = new JMenuItem("End Game", 'E');
		gameMenu.add(endGameMenuItem);
		endGameMenuItem.addActionListener((e) -> {
			endGame();
		});

		gameMenu.addSeparator();

		JMenuItem stepBackMenuItem = new JMenuItem("Step back");
		stepBackMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
		gameMenu.add(stepBackMenuItem);
		stepBackMenuItem.addActionListener((e) -> {
			gamePanel.getGameBoard().stepBack();
		});

		getJMenuBar().add(gameMenu);
	}

	private void addHelpMenu() {
		JMenu helpMenu = new JMenu("Help");

		JMenuItem aboutMenuItem = new JMenuItem("About", 'A');
		helpMenu.add(aboutMenuItem);
		aboutMenuItem.addActionListener((e) -> {
			AboutDialog aboutDialog = new AboutDialog(GameFrame.this);
			aboutDialog.setVisible(true);

		});

		getJMenuBar().add(helpMenu);
	}

	private void addHighScoreMenu() {
		JMenu highScoreMenu = new JMenu("High scores");
		highScoreMenu.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				showHighScoreDialog();
			}
		});

		getJMenuBar().add(highScoreMenu);
	}

	public void endGame() {
		saveHighScore();
		gamePanel.getGameBoard().newGame();
	}

	private void saveHighScore() {
		GameInfoBoard gameInfoBoard = gameBoard.getGameInfoBoard();

		// Stop the playing clock
		gameInfoBoard.setClockState(false);

		PlayerScoreHistory playerScoreHistory = PlayerScoreHistory.getInstance();

		// Player gets a new high score
		if (playerScoreHistory.isNewRecord(gameInfoBoard.getScore().getScore())) {
			String playerName = JOptionPane.showInputDialog(GameFrame.this,
					"You've got a high score. Please input your name", "New high score", JOptionPane.QUESTION_MESSAGE);
			if (playerName != null && !"".equals(playerName)) {
				// Add a new record to high score history
				playerScoreHistory.addHighScore(new PlayerScore(playerName, gameInfoBoard.getScore().getScore(),
						gameInfoBoard.getClock().toString()));
				playerScoreHistory.save();

				showHighScoreDialog();
			}
		}

		// Update highest score on the game status board
		gameInfoBoard.getHighestScore().setScore(playerScoreHistory.getHighestScore());
	}

	private void showHighScoreDialog() {
		HighScoreDialog highScoreDialog = new HighScoreDialog(GameFrame.this);
		highScoreDialog.setVisible(true);
	}

	private GameBoard gameBoard;
	private GamePanel gamePanel;
	private static final long serialVersionUID = -8199515970527728642L;
}
