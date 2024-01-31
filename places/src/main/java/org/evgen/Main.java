package org.evgen;

import org.evgen.gui.MainPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Places");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        frame.getContentPane().add(new MainPanel());
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}