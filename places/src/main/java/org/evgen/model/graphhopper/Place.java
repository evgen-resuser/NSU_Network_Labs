package org.evgen.model.graphhopper;

import com.google.gson.JsonElement;

public record Place(
    String name, String country, String city, String state,
    JsonElement point) {

    public String toString(){
        return name+( state != null ? ", " + state : "" )+
                " - "+country;
    }

    public String lat() {
        return String.valueOf(point.getAsJsonObject().get("lat"));
    }

    public String lng() {
        return String.valueOf(point.getAsJsonObject().get("lng"));
    }

}
