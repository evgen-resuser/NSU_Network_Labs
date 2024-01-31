package org.evgen.interfaces;

import org.evgen.model.int_places.IntPlace;

import java.util.List;

public interface IIntPlacesSearch {
    List<IntPlace> findIntPlaces(String lon, String lng);
}
