package org.evgen;

import org.evgen.interfaces.IView;
import org.evgen.interfaces.IModel;
import org.evgen.model.InfoType;
import org.evgen.model.ModelMain;
import org.evgen.model.graphhopper.Place;
import org.evgen.model.int_places.IntPlace;

public class Presenter {
    private final IView view;
    private final IModel model;

    public Presenter(IView view) {
        this.view = view;
        this.model = new ModelMain(this);
    }

    public void startSearch(String request) {
        model.searchByName(request);
    }

    public void provideInfo(Object info, InfoType type) {
        view.updateInfo(info, type);
    }

    public void searchBySelected(Place place){
        model.searchByPlace(place);
    }

    public void searchByIntPlace(IntPlace place) {
        model.searchByIntPlace(place);
    }
}
