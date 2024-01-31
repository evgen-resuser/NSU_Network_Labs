package org.evgen.interfaces;

import org.evgen.model.graphhopper.Place;

import java.util.List;

public interface IPlacesSearch {
    List<Place> getResponse(String name);
}
