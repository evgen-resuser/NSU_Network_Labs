package org.evgen.gui;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class MapPanel extends JPanel {
    private final ImageIcon noMap = new ImageIcon(ClassLoader.getSystemResource("no_map.png"));
    private final ImageIcon loadingMap = new ImageIcon(ClassLoader.getSystemResource("loading_map.png"));

    JLabel map = new JLabel(noMap);

    public MapPanel() {
        this.add(new JLabel("Карта:"));
        this.add(map);
    }

    public void setLoading() {
        map.setIcon(loadingMap);
    }

    public void setNoMap() {
        map.setIcon(noMap);
    }

    public void redrawMap(BufferedImage image){
        map.setIcon(new ImageIcon(image));
    }
}
