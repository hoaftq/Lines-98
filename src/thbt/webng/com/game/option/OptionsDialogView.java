package thbt.webng.com.game.option;

import thbt.webng.com.game.util.WindowUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Serial;

public class OptionsDialogView extends JDialog implements ActionListener, ItemListener {
    @Serial
    private static final long serialVersionUID = -3458511180350780500L;

    private final OptionsDialogPresenter presenter;

    private JRadioButton lineGameTypeButton;

    private JRadioButton blockGameTypeButton;

    private JRadioButton squareGameTypeButton;

    private JComboBox<NextBallsDisplayType> nextBallsDisplayTypesComboBox;

    private JCheckBox destroySoundCheckBox;

    private JCheckBox movingSoundCheckBox;

    private JCheckBox jumpingSoundCheckBox;

    private JButton cancelButton;

    private JButton okButton;

    public OptionsDialogView(JFrame owner, OptionsDialogPresenter presenter) {
        super(owner, "Options", true);
        this.presenter = presenter;

        addOptionsPanel();
        addActionsPanel();
        pack();

        WindowUtil.centerOwner(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == lineGameTypeButton) {
            presenter.onSetDefaultGameType(GameType.LINE);
        } else if (e.getSource() == blockGameTypeButton) {
            presenter.onSetDefaultGameType(GameType.BLOCK);
        } else if (e.getSource() == squareGameTypeButton) {
            presenter.onSetDefaultGameType(GameType.SQUARE);
        } else if (e.getSource() == nextBallsDisplayTypesComboBox) {
            presenter.onSetNextBallsDisplayType((NextBallsDisplayType) nextBallsDisplayTypesComboBox.getSelectedItem());
        } else if (e.getSource() == okButton) {
            presenter.onOK();
        } else if (e.getSource() == cancelButton) {
            presenter.onCancel();
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
        if (e.getItemSelectable() == destroySoundCheckBox) {
            presenter.onSetDestroySound(isSelected);
        } else if (e.getItemSelectable() == movingSoundCheckBox) {
            presenter.onSetMovementSound(isSelected);
        } else if (e.getItemSelectable() == jumpingSoundCheckBox) {
            presenter.onSetBallJumpingSound(isSelected);
        }
    }

    private void addOptionsPanel() {
        var optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        var defaultGameTypeLabel = new JLabel("Default game type:");
        optionsPanel.add(defaultGameTypeLabel);

        lineGameTypeButton = new JRadioButton("Line", presenter.getDefaultGameType() == GameType.LINE);
        lineGameTypeButton.addActionListener(this);
        blockGameTypeButton = new JRadioButton("Block", presenter.getDefaultGameType() == GameType.BLOCK);
        blockGameTypeButton.addActionListener(this);
        squareGameTypeButton = new JRadioButton("Square", presenter.getDefaultGameType() == GameType.SQUARE);
        squareGameTypeButton.addActionListener(this);
        var gameTypeGroup = new ButtonGroup();
        gameTypeGroup.add(lineGameTypeButton);
        gameTypeGroup.add(blockGameTypeButton);
        gameTypeGroup.add(squareGameTypeButton);

        var defaultGameTypePanel = new JPanel();
        defaultGameTypePanel.add(lineGameTypeButton);
        defaultGameTypePanel.add(blockGameTypeButton);
        defaultGameTypePanel.add(squareGameTypeButton);
        defaultGameTypePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        optionsPanel.add(defaultGameTypePanel);

        optionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        var nextBallDisplayTypeLabel = new JLabel("Next ball display type:");
        optionsPanel.add(nextBallDisplayTypeLabel);

        optionsPanel.add(Box.createRigidArea(new Dimension(0, 3)));

        nextBallsDisplayTypesComboBox = new JComboBox<>(NextBallsDisplayType.values());
        nextBallsDisplayTypesComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        nextBallsDisplayTypesComboBox.setSelectedItem(presenter.getNextBallsDisplayType());
        nextBallsDisplayTypesComboBox.addActionListener(this);
        optionsPanel.add(nextBallsDisplayTypesComboBox);

        optionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        destroySoundCheckBox = new JCheckBox("Play destroy sound", presenter.isDestroySound());
        destroySoundCheckBox.addItemListener(this);
        optionsPanel.add(destroySoundCheckBox);

        optionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        movingSoundCheckBox = new JCheckBox("Play moving sound", presenter.isMovementSound());
        movingSoundCheckBox.addItemListener(this);
        optionsPanel.add(movingSoundCheckBox);

        optionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        jumpingSoundCheckBox = new JCheckBox("Play jumping sound", presenter.isBallJumpingSound());
        jumpingSoundCheckBox.addItemListener(this);
        optionsPanel.add(jumpingSoundCheckBox);

        add(optionsPanel, BorderLayout.CENTER);
    }

    private void addActionsPanel() {
        var actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.LINE_AXIS));
        actionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        actionsPanel.add(Box.createHorizontalGlue());

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        actionsPanel.add(cancelButton);

        actionsPanel.add(Box.createHorizontalStrut(10));

        okButton = new JButton("OK");
        okButton.addActionListener(this);
        actionsPanel.add(okButton);

        add(actionsPanel, BorderLayout.SOUTH);
    }
}
