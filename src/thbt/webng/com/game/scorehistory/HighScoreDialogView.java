package thbt.webng.com.game.scorehistory;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.Serial;
import java.security.InvalidParameterException;
import java.util.List;

public class HighScoreDialogView extends JDialog {

    @Serial
    private static final long serialVersionUID = -7150760362536326108L;
    private final HighScoreDialogPresenter presenter;

    public HighScoreDialogView(JFrame frame, HighScoreDialogPresenter presenter) {
        super(frame, true);
        this.presenter = presenter;

        add(createHighScorePanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("High scores");
        setSize(320, 300);
        setLocationRelativeTo(frame);
        setResizable(false);
    }

    private JScrollPane createHighScorePanel() {
        var table = new JTable(new HighScoreTableModel(presenter.getTopScores()));
        var centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        return new JScrollPane(table);
    }

    private JPanel createActionPanel() {
        var actionPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener((e) -> {
            presenter.onOkButton();
        });

        actionPanel.add(okButton);
        return actionPanel;
    }

    private static class HighScoreTableModel extends AbstractTableModel {

        @Serial
        private static final long serialVersionUID = -7011685201149589747L;

        private final String[] columnNames = {"Name", "Score", "Play time"};

        private final List<PlayerScore> scores;

        public HighScoreTableModel(List<PlayerScore> scores) {
            this.scores = scores;
        }

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
            return switch (c) {
                case 0 -> scores.get(r).name();
                case 1 -> scores.get(r).score();
                case 2 -> scores.get(r).playTime();
                default -> throw new InvalidParameterException();
            };
        }
    }
}