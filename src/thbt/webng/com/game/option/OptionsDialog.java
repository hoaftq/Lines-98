package thbt.webng.com.game.option;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import thbt.webng.com.game.util.WindowUtil;

public class OptionsDialog extends JDialog implements ActionListener, ItemListener {

	private static final long serialVersionUID = -3458511180350780500L;

	private GameOptions gameInfo = GameOptions.getCurrentInstance().clone();

	private JRadioButton lineButton;

	private JRadioButton blockButton;

	private JRadioButton squareButton;

	private JComboBox<NextBallDisplayType> nextBallDisplayComboBox;

	private JCheckBox destroySoundCheckBox;

	private JCheckBox movingSoundCheckBox;

	private JCheckBox jumpingSoundCheckBox;

	private JButton cancelButton;

	private JButton okButton;

	public OptionsDialog(JFrame owner) {
		super(owner, "Options", true);

		JPanel optionPane = new JPanel();
		optionPane.setLayout(new BoxLayout(optionPane, BoxLayout.PAGE_AXIS));
		optionPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel defaultGameTypeLabel = new JLabel("Default game type:");
		optionPane.add(defaultGameTypeLabel);

		lineButton = new JRadioButton("Line", gameInfo.getDefaultGameType() == GameType.LINE);
		lineButton.addActionListener(this);
		blockButton = new JRadioButton("Block", gameInfo.getDefaultGameType() == GameType.BLOCK);
		blockButton.addActionListener(this);
		squareButton = new JRadioButton("Square", gameInfo.getDefaultGameType() == GameType.SQUARE);
		squareButton.addActionListener(this);
		ButtonGroup gameTypeGroup = new ButtonGroup();
		gameTypeGroup.add(lineButton);
		gameTypeGroup.add(blockButton);
		gameTypeGroup.add(squareButton);

		JPanel defaultGameTypePanel = new JPanel();
		defaultGameTypePanel.add(lineButton);
		defaultGameTypePanel.add(blockButton);
		defaultGameTypePanel.add(squareButton);
		defaultGameTypePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		optionPane.add(defaultGameTypePanel);

		optionPane.add(Box.createRigidArea(new Dimension(0, 5)));

		JLabel nextBallDisplayTypeLabel = new JLabel("Next ball display type:");
		optionPane.add(nextBallDisplayTypeLabel);

		optionPane.add(Box.createRigidArea(new Dimension(0, 3)));
		nextBallDisplayComboBox = new JComboBox<>(NextBallDisplayType.values());
		nextBallDisplayComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		nextBallDisplayComboBox.setSelectedItem(gameInfo.getNextBallDisplayType());
		nextBallDisplayComboBox.addActionListener(this);
		optionPane.add(nextBallDisplayComboBox);

		optionPane.add(Box.createRigidArea(new Dimension(0, 5)));
		destroySoundCheckBox = new JCheckBox("Play destroy sound", gameInfo.isDestroySound());
		destroySoundCheckBox.addItemListener(this);
		optionPane.add(destroySoundCheckBox);

		optionPane.add(Box.createRigidArea(new Dimension(0, 5)));
		movingSoundCheckBox = new JCheckBox("Play moving sound", gameInfo.isMovementSound());
		movingSoundCheckBox.addItemListener(this);
		optionPane.add(movingSoundCheckBox);

		optionPane.add(Box.createRigidArea(new Dimension(0, 5)));
		jumpingSoundCheckBox = new JCheckBox("Play jumping sound", gameInfo.isBallJumpingSound());
		jumpingSoundCheckBox.addItemListener(this);
		optionPane.add(jumpingSoundCheckBox);
		add(optionPane, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);

		buttonPane.add(Box.createHorizontalStrut(10));
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		buttonPane.add(okButton);
		add(buttonPane, BorderLayout.SOUTH);

		pack();
		WindowUtil.centerOwner(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == lineButton) {
			gameInfo.setDefaultGameType(GameType.LINE);
		} else if (e.getSource() == blockButton) {
			gameInfo.setDefaultGameType(GameType.BLOCK);
		} else if (e.getSource() == squareButton) {
			gameInfo.setDefaultGameType(GameType.SQUARE);
		} else if (e.getSource() == nextBallDisplayComboBox) {
			gameInfo.setNextBallDisplayType((NextBallDisplayType) nextBallDisplayComboBox.getSelectedItem());
		} else if (e.getSource() == okButton) {
			GameOptions.setCurrentInstance(gameInfo);
			setVisible(false);
			dispose();
		} else if (e.getSource() == cancelButton) {
			setVisible(false);
			dispose();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
		if (e.getItemSelectable() == destroySoundCheckBox) {
			gameInfo.setDestroySound(isSelected);
		} else if (e.getItemSelectable() == movingSoundCheckBox) {
			gameInfo.setMovementSound(isSelected);
		} else if (e.getItemSelectable() == jumpingSoundCheckBox) {
			gameInfo.setBallJumpingSound(isSelected);
		}
	}
}
