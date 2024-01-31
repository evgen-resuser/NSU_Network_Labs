package org.evgen.gui.components;

import javax.swing.*;
import java.awt.*;

public class Separator extends JPanel {
    public Separator() {
        this.setPreferredSize(new Dimension(380, 5));
        this.setLayout(new BorderLayout());

        this.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.CENTER);
    }
}
