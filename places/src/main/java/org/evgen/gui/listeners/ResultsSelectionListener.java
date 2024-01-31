package org.evgen.gui.listeners;

import org.evgen.interfaces.IView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultsSelectionListener implements ActionListener {
    private final IView view;

    public ResultsSelectionListener(IView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.getPlaceInfo();
    }
}
