package thbt.webng.com.game.scorehistory;

import thbt.webng.com.game.util.WindowUtil;

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

        addHighScoreTable();
        addOkButton();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("High scores");
        setSize(320, 300);
        WindowUtil.centerOwner(this);
        setResizable(false);
    }

    private void addHighScoreTable() {
        var table = new JTable(new HighScoreTableModel(presenter.getTopScores()));
        var centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        var northPane = new JScrollPane(table);
        add(northPane, BorderLayout.CENTER);
    }

    private void addOkButton() {
        var southPane = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener((e) -> {
            presenter.onOkButton();
        });

        southPane.add(okButton);
        add(southPane, BorderLayout.SOUTH);
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