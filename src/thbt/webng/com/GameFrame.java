package thbt.webng.com;

import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

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
import thbt.webng.com.game.status.GameInfoBoard;
import thbt.webng.com.game.status.PlayerScore;
import thbt.webng.com.game.status.PlayerScoreHistory;

public class GameFrame extends JFrame {

	public GameFrame() {
		setTitle("Lines");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		gamePanel = new GamePanel(this);
		add(gamePanel);

		GameBoard gameBoard = new GameBoard(gamePanel);
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
			gamePanel.getGameBoard().newGame();
		});

		gameMenu.addSeparator();

		JMenuItem newLinesMenuItem = new JMenuItem("New Lines Game", 'L');
		gameMenu.add(newLinesMenuItem);
		newLinesMenuItem.addActionListener((e) -> {
			GameInfo.getInstance().setGameType(GameType.Lines);
			gamePanel.getGameBoard().newGame();
		});

		JMenuItem newSquaresMenuItem = new JMenuItem("New Squares Game", 'S');
		gameMenu.add(newSquaresMenuItem);
		newSquaresMenuItem.addActionListener((e) -> {
			GameInfo.getInstance().setGameType(GameType.Squares);
			gamePanel.getGameBoard().newGame();
		});

		JMenuItem newBlocksMenuItem = new JMenuItem("New Blocks Game", 'B');
		gameMenu.add(newBlocksMenuItem);
		newBlocksMenuItem.addActionListener((e) -> {
			GameInfo.getInstance().setGameType(GameType.Blocks);
			gamePanel.getGameBoard().newGame();
		});

		gameMenu.addSeparator();

		JMenuItem saveGameMenuItem = new JMenuItem("Save Game", 'S');
		gameMenu.add(saveGameMenuItem);
		saveGameMenuItem.addActionListener((e) -> {
			gamePanel.getGameBoard().saveGame();
		});

		JMenuItem loadGameMenuItem = new JMenuItem("Load Game", 'L');
		gameMenu.add(loadGameMenuItem);
		loadGameMenuItem.addActionListener((e) -> {
			gamePanel.getGameBoard().loadGame();
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
//		int minScore;
//
//		while (true) {
//			try {
//				minScore = HighScoreUtil.getSmallestScore();
//				break;
//			} catch (NumberFormatException | IOException e1) {
//				e1.printStackTrace();
//				if (JOptionPane.showConfirmDialog(GameFrame.this, "Occur error while connect to server, retry?",
//						"Lines", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
//					return;
//				}
//			}
//		}
//
//		GameInfoBoard gameInfoBoard = gamePanel.getGameBoard().getGameInfoBoard();
//		if (gameInfoBoard.getScore().getScore() > minScore) {
//			String playerName = JOptionPane.showInputDialog(GameFrame.this, "Please input your name", "Game Over",
//					JOptionPane.QUESTION_MESSAGE);
//			if (playerName != null) {
//				while (true) {
//					String status = HighScoreUtil.sendHighScore(playerName, gameInfoBoard.getScore().getScore(),
//							gameInfoBoard.getClock().toString());
//					if ("true".equals(status)) {
//						break;
//					}
//
//					if (JOptionPane.showConfirmDialog(GameFrame.this,
//							"Occur error while send your information to server, retry?", "Lines",
//							JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
//						break;
//					}
//
//				}
//			}
//		}
		
		GameInfoBoard gameInfoBoard = gamePanel.getGameBoard().getGameInfoBoard();
		PlayerScoreHistory playerScoreHistory = PlayerScoreHistory.getInstance();
		
		// Player gets a new high score
		if(playerScoreHistory.isNewRecord(gameInfoBoard.getScore().getScore())) {
			String playerName = JOptionPane.showInputDialog(GameFrame.this, "You've got a high score. Please input your name", "Game Over", JOptionPane.QUESTION_MESSAGE);
			if(playerName != null) {
				// Add a new record to high score history
				playerScoreHistory.addHighScore(new PlayerScore(playerName, gameInfoBoard.getScore().getScore(), gameInfoBoard.getClock().toString()));
				playerScoreHistory.save();
			}
		}

		showHighScoreDialog();

//		if (gameInfoBoard.getScore().getScore() > gameInfoBoard.getHigherScore().getScore()) {
//			gameInfoBoard.getHigherScore().setScore(gameInfoBoard.getScore().getScore());
//		}
		
		// Update highest score on the game status board
		gameInfoBoard.getHighestScore().setScore(playerScoreHistory.getHighestScore());
		
		gamePanel.getGameBoard().newGame();
	}

	private void showHighScoreDialog() {
		HighScoreDialog highScoreDialog = new HighScoreDialog(GameFrame.this);
		highScoreDialog.display();
	}

	private GamePanel gamePanel;
	private static final long serialVersionUID = -8199515970527728642L;
}
