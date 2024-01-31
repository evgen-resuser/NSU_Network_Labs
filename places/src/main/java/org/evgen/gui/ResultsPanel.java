package org.evgen.gui;

import org.evgen.gui.listeners.ResultsSelectionListener;
import org.evgen.model.graphhopper.Place;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultsPanel extends JPanel {

    private final JComboBox<Object> results = new JComboBox<>();

    public ResultsPanel(ResultsSelectionListener listener) {
        results.setPrototypeDisplayValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        results.setEnabled(false);
        results.addItem("...");
        results.addActionListener(listener);

        this.setPreferredSize(new Dimension(400, 40));
        this.setMaximumSize(this.getPreferredSize());

        this.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        this.add(new JLabel("Результаты: "));
        this.add(results);
    }

    public void setResults(List<Place> list) {
        results.removeAllItems();

        if (list == null) {
            System.out.println("Error while adding results: null list!");
            return;
        }

        if (list.isEmpty()) {
            results.setEnabled(false);
            results.addItem("Нет результатов");
            return;
        }

        for (Place place : list) {
            results.addItem(place);
        }
        results.setEnabled(true);
    }

    public Place getSelected() {
        return (Place)results.getSelectedItem();
    }

}
