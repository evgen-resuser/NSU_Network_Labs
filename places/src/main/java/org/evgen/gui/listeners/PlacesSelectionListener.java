package org.evgen.gui.listeners;

import org.evgen.interfaces.IDescriptionSearch;
import org.evgen.interfaces.IView;
import org.evgen.model.int_places.IntPlace;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PlacesSelectionListener implements ListSelectionListener {

    private final IView view;
    private JList<IntPlace> placeJList;

    public PlacesSelectionListener(IView view) {
        this.view = view;
    }

    public void setPlaceJList(JList<IntPlace> placeJList) {
        this.placeJList = placeJList;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!placeJList.getValueIsAdjusting()) {
            view.getPlaceDescription(placeJList.getSelectedValue());
        }
    }
}
