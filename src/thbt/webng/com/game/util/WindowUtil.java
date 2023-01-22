package thbt.webng.com.game.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * Window utility
 */
public final class WindowUtil {

	/**
	 * Make static class
	 */
	private WindowUtil() {
	}

	/**
	 * Set window location to center owner
	 * 
	 * @param window
	 *            window what set to center owner
	 */
	public static void centerOwner(Window window) {
		Component onwner = window.getOwner();
		int x, y;

		// If owner is null, set dialog position to center screen
		if (onwner == null) {
			Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
			x = (int) ((scrSize.getWidth() - window.getWidth()) / 2);
			y = (int) ((scrSize.getHeight() - window.getHeight()) / 2);
		} else {

			// Set dialog position to center owner
			x = onwner.getX() + (onwner.getWidth() - window.getWidth()) / 2;
			y = onwner.getY() + (onwner.getHeight() - window.getHeight()) / 2;
		}

		// Ensure dialog in screen
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;

		window.setLocation(x, y);
	}

}
