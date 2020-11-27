package thbt.webng.com.game.common;

import java.awt.Color;
import java.util.Random;

public final class ColorUtil {
	private final static Color[] lineColorArray = new Color[] { Color.RED,
			Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN, Color.MAGENTA,
			new Color(160, 0, 0) };

	public static Color getRandomColor() {
		Random random = new Random();
		return lineColorArray[random.nextInt(lineColorArray.length)];
	}
}
