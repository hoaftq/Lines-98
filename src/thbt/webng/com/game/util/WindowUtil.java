package thbt.webng.com.game.util;

import java.awt.*;

public final class WindowUtil {

    /**
     * Make static class
     */
    private WindowUtil() {
    }

    /**
     * Set window location to owner center
     *
     * @param window window that is set to owner center
     */
    public static void centerOwner(Window window) {
        var owner = window.getOwner();
        int x, y;

        // If the dialog doesn't have an owner, put the dialog to screen center
        if (owner == null) {
            var scrSize = Toolkit.getDefaultToolkit().getScreenSize();
            x = (int) ((scrSize.getWidth() - window.getWidth()) / 2);
            y = (int) ((scrSize.getHeight() - window.getHeight()) / 2);
        } else {

            // Put the dialog to owner center
            x = owner.getX() + (owner.getWidth() - window.getWidth()) / 2;
            y = owner.getY() + (owner.getHeight() - window.getHeight()) / 2;
        }

        // Ensure the dialog is in screen
        if (x < 0)
            x = 0;
        if (y < 0)
            y = 0;

        window.setLocation(x, y);
    }
}
