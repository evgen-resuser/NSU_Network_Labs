package org.evgen.gui;

import org.evgen.gui.listeners.ButtonListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class SearchPanel extends JPanel {

    private final JTextField searchBox = new JTextField();

    public SearchPanel(ButtonListener listener) {

        JButton button = new JButton("Поиск");
        button.addActionListener(listener);

        this.setBorder(new LineBorder(new Color(255, 153, 0), 2));
        this.setBackground(Color.ORANGE);
        this.setPreferredSize(new Dimension(390, 40));
        this.setMaximumSize(this.getPreferredSize());
        searchBox.setColumns(20);

        this.add(new JLabel("Искать:"));
        this.add(searchBox);
        this.add(button);
    }

    public String getRequest() {
        String res = searchBox.getText();
        searchBox.setText("");
        return res;
    }

}
