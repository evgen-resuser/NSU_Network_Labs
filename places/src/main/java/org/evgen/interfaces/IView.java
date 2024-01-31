package org.evgen.interfaces;

import org.evgen.model.InfoType;
import org.evgen.model.int_places.IntPlace;

public interface IView {
    void search();
    void getPlaceInfo();
    void updateInfo(Object info, InfoType type);
    void getPlaceDescription(IntPlace place);
}
