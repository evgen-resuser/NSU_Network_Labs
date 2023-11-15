package com.evgen.places_search.graphhopper;

import com.google.gson.JsonObject;

public class Place {

    private String name;
    private String country;
    private String city;
    private String state;
    private String lat;
    private String lng;
    private JsonObject point;

    public Place(String name, String country, String city, String state, JsonObject point) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.state = state;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public JsonObject getPoint() {
        return point;
    }

    public String[] parseCoordinates() {

        String[] res = new String[2];

        res[0] = String.valueOf(point.get("lat"));
        res[1] = String.valueOf(point.get("lng"));

        return res;
    }

    public String toString(){
        return name+( state != null ? ", " + state : "" )+
                " - "+country;
    }
}
