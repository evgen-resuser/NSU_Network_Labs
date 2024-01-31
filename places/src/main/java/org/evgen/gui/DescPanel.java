package org.evgen.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DescPanel extends JPanel {

    private final JTextPane textPane;

    public DescPanel() {
        this.setPreferredSize(new Dimension(440, 210));
        this.setMaximumSize(this.getPreferredSize());

        JScrollPane pane = new JScrollPane();
        textPane = new JTextPane();
        textPane.setContentType("text/html");
        pane.setViewportView(textPane);
        pane.createVerticalScrollBar();
        textPane.setText("<div style=\"font-family: Arial;\">Попробуйте найти что-нибудь!</div>");
        textPane.setEditable(false);
        textPane.setPreferredSize(new Dimension(430, 200));

        textPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            }
        });

        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            }
        });

        this.add(pane);
    }

    public void updateInfo(String info) {
        textPane.removeAll();
        textPane.setText("<html><div style=\"font-family: Arial;\">"+info+"</div></html>");
    }

    public void clear() {
        textPane.removeAll();
    }

}
