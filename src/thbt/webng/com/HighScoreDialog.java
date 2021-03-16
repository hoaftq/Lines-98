package thbt.webng.com;

import java.awt.BorderLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidParameterException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import thbt.webng.com.game.common.WindowUtil;
import thbt.webng.com.game.status.PlayerScore;
import thbt.webng.com.game.status.PlayerScoreHistory;

public class HighScoreDialog extends JDialog {

	public HighScoreDialog(JFrame frame) {
		super(frame, true);

		// JPanel northPanel = new JPanel();
		// label = new JLabel("Please wait...");
		// northPanel.add(label);

		JTable table = new JTable(new HighScoreTableModel());
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.setDefaultRenderer(Object.class, centerRenderer);

		JScrollPane northPanel = new JScrollPane(table);

		// northPanel.add(table);
		add(northPanel, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				HighScoreDialog.this.setVisible(false);
				HighScoreDialog.this.dispose();
			}
		});

		panel.add(okButton);
		add(panel, BorderLayout.SOUTH);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("High scores");
		setSize(320, 300);
		WindowUtil.centerOwner(this);
		setResizable(false);
	}

	public void display() {
		// new Thread() {
		// @Override
		// public void run() {
		// label.setText(HighScoreUtil.getTopHighScore());
		// }
		// }.start();
		setVisible(true);
	}

	private JLabel label;

	private static final long serialVersionUID = -7150760362536326108L;

	private class HighScoreTableModel extends AbstractTableModel {

		private static final long serialVersionUID = -7011685201149589747L;

		private final String[] columnNames = { "Name", "Score", "Play time" };

		private List<PlayerScore> scores = PlayerScoreHistory.getInstance().getTopScores();

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int c) {
			return columnNames[c];
		}

		@Override
		public int getRowCount() {
			return scores.size();
		}

		@Override
		public Object getValueAt(int r, int c) {
			switch (c) {
			case 0:
				return scores.get(r).getName();
			case 1:
				return scores.get(r).getScore();
			case 2:
				return scores.get(r).getPlayTime();

			}
			throw new InvalidParameterException();
		}

	}
}
