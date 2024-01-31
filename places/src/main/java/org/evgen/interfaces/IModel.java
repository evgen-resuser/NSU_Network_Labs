package org.evgen.interfaces;

import org.evgen.model.graphhopper.Place;
import org.evgen.model.int_places.IntPlace;

public interface IModel {
    void searchByName(String name);
    void searchByPlace(Place place);
    void searchByIntPlace(IntPlace place);
}
