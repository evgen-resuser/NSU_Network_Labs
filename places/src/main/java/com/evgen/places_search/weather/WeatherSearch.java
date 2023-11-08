package com.evgen.places_search.weather;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WeatherSearch implements Runnable{

    String[] coords;
    Weather result;

    public void setCoords(String[] coords) {
        this.coords = coords;
    }

    public void doSearch(String[] coords){
        String urlAPI = "https://api.openweathermap.org/data/2.5/weather?lat="+coords[0]+"&lon="+coords[1]+
                "&appid=a133af35b4075ae2a3a3bea620e65eab&lang=ru&units=metric";
        getJSON(urlAPI);
    }

    private void parseJSON(String content) {
        result = new Weather();
        JsonObject obj = JsonParser.parseString(content).getAsJsonObject();

        JsonArray a = obj.getAsJsonArray("weather");
        JsonObject propertiesJson = (JsonObject) a.get(0);
        String value = propertiesJson.get("description").getAsString();

        result.setDescription(value);

        JsonObject obj1 = obj.getAsJsonObject("main");

        result.setTemp(String.valueOf(obj1.get("temp")));
        result.setTempMax(String.valueOf(obj1.get("temp_max")));
        result.setTempMin(String.valueOf(obj1.get("temp_min")));
        result.setFeelsLike(String.valueOf(obj1.get("feels_like")));
        result.setHumidity(String.valueOf(obj1.get("humidity")));
        result.setPressure(String.valueOf(obj1.get("pressure")));

        obj1 = obj.getAsJsonObject("wind");
        result.setWindSpeed(String.valueOf(obj1.get("speed")));

    }

    private void getJSON(String urlAPI){
        try {
            URL url = new URL(urlAPI);
            String content = IOUtils.toString(url, StandardCharsets.UTF_8);
            parseJSON(content);

        } catch (IOException e) {
            System.out.println("weather error: "+e.getMessage());
        }
    }

    public Weather getResult() {
        return result;
    }

    @Override
    public void run() {
        if (coords != null){
            doSearch(coords);
        }
    }
}
