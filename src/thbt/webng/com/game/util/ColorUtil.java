package thbt.webng.com.game.util;

import java.awt.*;
import java.util.Random;

public final class ColorUtil {
    private final static Random random = new Random();

    private final static Color[] colors = new Color[]{
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.YELLOW,
            Color.CYAN,
            Color.MAGENTA,
            new Color(160, 0, 0)
    };

    public static Color getRandomColor() {
        return colors[random.nextInt(colors.length)];
    }
}
