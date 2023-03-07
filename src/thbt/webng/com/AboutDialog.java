package thbt.webng.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Serial;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutDialog extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String VERSION = "1.0.0";
    private static final String THBT_WEB_ADDRESS = "https://github.com/hoaftq/Lines-98";

    public AboutDialog(JFrame owner) {
        super(owner, "About", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(createInfoPanel(), BorderLayout.NORTH);
        add(createLinkPanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    private JPanel createInfoPanel() {
        var infoLabel = new JLabel("""
                <html>
                	<table>
                		<tr>
                			<td colspan=2 align=center><font size=5 color=Red>Lines 98</font></td>
                		</tr>
                		<tr>
                			<td>Write by:</td>
                			<td>hoaftq</td>
                		</tr>
                		<tr>
                			<td>Version:</td>
                			<td>%s</td>
                		</tr>
                	</table>
                </html>
                """.formatted(VERSION));
        var infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.add(infoLabel);
        return infoPanel;
    }

    private JPanel createLinkPanel() {
        var normalLinkLabel = "<html><a href='%s'>%s</a></html>".formatted(THBT_WEB_ADDRESS, THBT_WEB_ADDRESS);

        var linkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linkPanel.add(new JLabel("Source:"));

        var linkLabel = new JLabel(normalLinkLabel);
        linkLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Make entered/exited animate and browse the link when clicked
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openHomePage();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ((JLabel) e.getSource()).setText(
                        "<html><a href='%s'><i>%s</i></a></html>".formatted(THBT_WEB_ADDRESS, THBT_WEB_ADDRESS));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JLabel) e.getSource()).setText(normalLinkLabel);
            }

            private void openHomePage() {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI(THBT_WEB_ADDRESS));
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        linkPanel.add(linkLabel);
        return linkPanel;
    }


    private JPanel createActionPanel() {
        var actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        var okButton = new JButton("OK");
        okButton.addActionListener((e) -> {
            setVisible(false);
            dispose();
        });
        actionPanel.add(okButton);
        return actionPanel;
    }
}
