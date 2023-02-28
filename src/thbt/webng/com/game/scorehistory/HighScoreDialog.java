package thbt.webng.com.game.scorehistory;

import thbt.webng.com.game.util.WindowUtil;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.security.InvalidParameterException;
import java.util.List;

public class HighScoreDialog extends JDialog {

    private static final long serialVersionUID = -7150760362536326108L;

    public HighScoreDialog(JFrame frame) {
        super(frame, true);

        JTable table = new JTable(new HighScoreTableModel());
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane northPane = new JScrollPane(table);
        add(northPane, BorderLayout.CENTER);

        JPanel southPane = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener((e) -> {
            HighScoreDialog.this.setVisible(false);
            HighScoreDialog.this.dispose();
        });

        southPane.add(okButton);
        add(southPane, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("High scores");
        setSize(320, 300);
        WindowUtil.centerOwner(this);
        setResizable(false);
    }

    private class HighScoreTableModel extends AbstractTableModel {

        private static final long serialVersionUID = -7011685201149589747L;

        private final String[] columnNames = {"Name", "Score", "Play time"};

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
            return switch (c) {
                case 0 -> scores.get(r).name();
                case 1 -> scores.get(r).score();
                case 2 -> scores.get(r).playTime();
                default -> throw new InvalidParameterException();
            };
        }
    }
}
