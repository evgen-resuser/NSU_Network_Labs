package org.evgen.model;

import org.evgen.Presenter;
import org.evgen.interfaces.*;
import org.evgen.model.description.OpenTripMapInfoSearch;
import org.evgen.model.graphhopper.GraphhopperSearch;
import org.evgen.model.graphhopper.Place;
import org.evgen.model.int_places.IntPlace;
import org.evgen.model.int_places.OpenTripMapSearch;
import org.evgen.model.map.MapQuestSearch;
import org.evgen.model.weather.OpenWeatherSearch;

import java.util.concurrent.CompletableFuture;

public class ModelMain implements IModel {
    private final Presenter presenter;

    private final IPlacesSearch graphhopperSearch = new GraphhopperSearch();
    private final IWeatherSearch weatherSearch = new OpenWeatherSearch();
    private final IIntPlacesSearch intPlacesSearch = new OpenTripMapSearch();
    private final IDescriptionSearch infoSearch = new OpenTripMapInfoSearch();
    private final IMapSearch mapSearch = new MapQuestSearch();

    public ModelMain(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void searchByName(String name) {
        CompletableFuture.runAsync( () -> presenter.provideInfo(graphhopperSearch.getResponse(name), InfoType.RESULTS));
    }

    @Override
    public void searchByPlace(Place place) {

        CompletableFuture.runAsync( () -> presenter.provideInfo(
                weatherSearch.getWeather(place.lat(), place.lng()), InfoType.WEATHER));

        CompletableFuture.runAsync( () -> presenter.provideInfo(
                intPlacesSearch.findIntPlaces(place.lat(), place.lng()), InfoType.INT_PLACES
        ));

        CompletableFuture.runAsync( () -> presenter.provideInfo(
                mapSearch.getMap(place.lat(), place.lng()), InfoType.MAP
        ));
    }

    @Override
    public void searchByIntPlace(IntPlace place) {
        if (place == null || place.xid() == null) return;
        CompletableFuture.runAsync( () -> presenter.provideInfo(
                infoSearch.getDescription(place), InfoType.DESCRIPTION
        ));
    }
}
