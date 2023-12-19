package org.evgen.gui;

import java.awt.*;

public class Colors {
    private final Color[] colors = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.PINK,
            Color.MAGENTA, Color.WHITE, new Color(0, 166, 255)
    };

    public Color getColor(int i) {
        if (i > 9 || i < 0) {
            return colors[0];
        }
        return colors[i];
    }
}
