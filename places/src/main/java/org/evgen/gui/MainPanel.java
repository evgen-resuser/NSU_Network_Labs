package org.evgen.gui;

import org.evgen.Presenter;
import org.evgen.gui.components.Separator;
import org.evgen.gui.listeners.ButtonListener;
import org.evgen.gui.listeners.PlacesSelectionListener;
import org.evgen.gui.listeners.ResultsSelectionListener;
import org.evgen.interfaces.IView;
import org.evgen.model.InfoType;
import org.evgen.model.graphhopper.Place;
import org.evgen.model.int_places.IntPlace;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

public class MainPanel extends JPanel implements IView {

    private final transient Presenter presenter;

    private final SearchPanel searchPanel;
    private final ResultsPanel resultsPanel;
    private final WeatherIntListPanel weatherIntListPanel;
    private final DescPanel descPanel;
    private final MapPanel mapPanel;

    public MainPanel() {
        presenter = new Presenter(this);

        searchPanel = new SearchPanel(new ButtonListener(this));
        resultsPanel = new ResultsPanel(new ResultsSelectionListener(this));
        weatherIntListPanel = new WeatherIntListPanel(new PlacesSelectionListener(this));
        descPanel = new DescPanel();
        mapPanel = new MapPanel();

        init();
    }

    private void init() {
        this.setPreferredSize(new Dimension(450, 700));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.add(searchPanel);
        this.add(resultsPanel);
        this.add(new Separator());
        this.add(weatherIntListPanel);
        this.add(new Separator());
        this.add(descPanel);
        this.add(mapPanel);
    }

    @Override
    public void search() {
        String request = searchPanel.getRequest();
        if (!Objects.equals(request, "")) {
            presenter.startSearch(request);
            mapPanel.setLoading();
        }
    }

    @Override
    public void getPlaceInfo() {
        Place selected = resultsPanel.getSelected();
        presenter.searchBySelected(selected);
    }

    @Override
    public void updateInfo(Object info, InfoType type) {
        switch (type) {
            case WEATHER -> weatherIntListPanel.updateWeather(info.toString());
            case INT_PLACES -> weatherIntListPanel.updatePlaces((List<IntPlace>)info);
            case DESCRIPTION -> descPanel.updateInfo((String)info);
            case MAP -> {
                if (info == null) mapPanel.setNoMap();
                else mapPanel.redrawMap((BufferedImage)info);
            }
            case RESULTS -> resultsPanel.setResults((List<Place>)info);
        }
    }

    @Override
    public void getPlaceDescription(IntPlace place) {
        descPanel.updateInfo("Ищем подробности...");
        presenter.searchByIntPlace(place);
    }
}
