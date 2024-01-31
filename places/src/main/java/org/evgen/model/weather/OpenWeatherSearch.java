package org.evgen.model.weather;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;
import org.evgen.interfaces.IWeatherSearch;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class OpenWeatherSearch implements IWeatherSearch {

    private static final double PA_MM = 0.75;

    @Override
    public Weather getWeather(String lat, String lng) {

        String urlAPI = "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lng+
                "&appid=a133af35b4075ae2a3a3bea620e65eab&lang=ru&units=metric";
        try {
            URL url = new URL(urlAPI);
            String content = IOUtils.toString(url, StandardCharsets.UTF_8);
            return parseJson(content);

        } catch (IOException e) {
            System.out.println("weather error: "+e.getMessage());
        }

        return null;
    }

    private Weather parseJson(String content){
        JsonObject obj = JsonParser.parseString(content).getAsJsonObject();

        JsonArray a = obj.getAsJsonArray("weather");
        JsonObject propertiesJson = (JsonObject) a.get(0);
        String description = propertiesJson.get("description").getAsString();

        JsonObject obj1 = obj.getAsJsonObject("main");

        String tmp = (String.valueOf(obj1.get("temp")));
        String tmpMax = (String.valueOf(obj1.get("temp_max")));
        String tmpMin = (String.valueOf(obj1.get("temp_min")));
        String feelsLike = (String.valueOf(obj1.get("feels_like")));
        String humidity = (String.valueOf(obj1.get("humidity")));

        String pressurePa = String.valueOf(obj1.get("pressure"));
        String pressure = (String.valueOf((Double.parseDouble(pressurePa)*PA_MM)));

        obj1 = obj.getAsJsonObject("wind");
        String wind = (String.valueOf(obj1.get("speed")));

        return new Weather(description, tmp, feelsLike, tmpMin, tmpMax, pressure, humidity, wind);
    }
}
