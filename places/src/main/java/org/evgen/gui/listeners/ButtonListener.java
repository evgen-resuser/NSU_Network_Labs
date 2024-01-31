package org.evgen.gui.listeners;

import org.evgen.interfaces.IView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {

    private final IView view;

    public ButtonListener(IView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.search();
    }
}
