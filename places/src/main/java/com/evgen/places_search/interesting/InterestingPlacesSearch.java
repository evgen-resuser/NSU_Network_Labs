package com.evgen.places_search.interesting;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class InterestingPlacesSearch implements Runnable{

    private String[] coords;
    private List<IntPlace> results;

    //moscow [55.7505412, 37.6174782] - lat lon

    public void setCoords(String[] coords) {
        this.coords = coords;
    }

    public List<IntPlace> getResults() {
        return results;
    }

    public void doSearch(){
        String urlAPI = "http://api.opentripmap.com/0.1/ru/places/radius?lang=ru&radius=1000&lon="+coords[1]+"&lat="+
                coords[0]+"&kinds=interesting_places&limit=50&format=json&apikey=5ae2e3f221c38a28845f05b65ae560f39d9a453063f501bb19614a9b";
        String content = getJSONFromResponse(urlAPI);
        parseJSON(content);
    }

    private String getJSONFromResponse(String urlAPI){
        String content = null;
        try {
            URL url = new URL(urlAPI);
            content = IOUtils.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("failed to connect");
        }
        return content;
    }

    private void parseJSON(String content) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<IntPlace>>() {}.getType();
        results = gson.fromJson(content, listType);
    }

    @Override
    public void run() {
        if (coords != null){
            doSearch();
            deleteTrashResults();
        }
    }

    private void deleteTrashResults(){
        results.removeIf(place -> place.getName().isEmpty());
    }
}
