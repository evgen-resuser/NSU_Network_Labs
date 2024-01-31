package org.evgen.model.int_places;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.evgen.interfaces.IIntPlacesSearch;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

public class OpenTripMapSearch implements IIntPlacesSearch {
    @Override
    public List<IntPlace> findIntPlaces(String lat, String lng) {
        String urlAPI = "http://api.opentripmap.com/0.1/ru/places/radius?lang=ru&radius=1000&lon="+lng+"&lat="+
                lat+"&kinds=interesting_places&limit=50&format=json&apikey=5ae2e3f221c38a28845f05b65ae560f39d9a453063f501bb19614a9b";
        String content;
        try {
            URL url = new URL(urlAPI);
            content = IOUtils.toString(url, StandardCharsets.UTF_8);
            return parseJSON(content);
        } catch (IOException e) {
            System.out.println("failed to connect");
        }

        return Collections.emptyList();
    }

    private List<IntPlace> parseJSON(String content) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<IntPlace>>() {}.getType();
        return gson.fromJson(content, listType);
    }
}
