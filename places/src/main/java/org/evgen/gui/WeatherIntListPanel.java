package org.evgen.gui;

import org.evgen.gui.listeners.PlacesSelectionListener;
import org.evgen.model.int_places.IntPlace;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.util.List;
import java.awt.*;

public class WeatherIntListPanel extends JPanel {

    private JTextPane weatherPane;
    private JList<IntPlace> interestingPlaces;
    private final PlacesSelectionListener listener;

    public WeatherIntListPanel(PlacesSelectionListener listener) {

        this.listener = listener;

        this.setLayout(new GridLayout(1, 2));
        this.add(initWeatherPanel());
        this.add(initIntListPanel());
    }

    private JPanel initWeatherPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel("Погода в этом месте:");

        weatherPane = new JTextPane();
        weatherPane.setEditable(false);
        weatherPane.setContentType("text/html");
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(weatherPane);

        JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.X_AXIS));
        weatherPanel.add(label);

        panel.add(weatherPanel);
        panel.add(scrollPane);

        return panel;
    }

    private JPanel initIntListPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(CENTER_ALIGNMENT);

        JScrollPane pane = new JScrollPane();
        interestingPlaces = new JList<>();
        interestingPlaces.setDragEnabled(false);
        interestingPlaces.setVisibleRowCount(8);
        listener.setPlaceJList(interestingPlaces);
        interestingPlaces.addListSelectionListener(listener);

        pane.createVerticalScrollBar();
        pane.setViewportView(interestingPlaces);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.add(new JLabel("Интересные места рядом:"));

        panel.add(panel1);
        panel.add(pane);

        return panel;
    }

    public void updateWeather(String content) {
        weatherPane.removeAll();
        weatherPane.setText(content);
    }

    public void updatePlaces(List<IntPlace> places) {
        interestingPlaces.removeAll();
        interestingPlaces.setListData(places.toArray(new IntPlace[0]));
    }

    public void clear() {
        interestingPlaces.removeAll();
        weatherPane.removeAll();
    }
}
