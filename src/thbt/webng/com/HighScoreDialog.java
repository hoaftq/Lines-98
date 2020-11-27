package thbt.webng.com;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import thbt.webng.com.game.common.WindowUtil;
import thbt.webng.com.game.info.HighScoreUtil;

public class HighScoreDialog extends JDialog {

	public HighScoreDialog(JFrame frame) {
		super(frame, true);

		JPanel northPanel = new JPanel();
		label = new JLabel("Please wait...");
		northPanel.add(label);
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
		new Thread() {
			@Override
			public void run() {
				label.setText(HighScoreUtil.getTopHighScore());
			}
		}.start();
		setVisible(true);
	}

	private JLabel label;

	private static final long serialVersionUID = -7150760362536326108L;
}
