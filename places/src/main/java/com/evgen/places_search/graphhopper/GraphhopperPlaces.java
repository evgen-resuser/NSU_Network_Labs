package com.evgen.places_search.graphhopper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GraphhopperPlaces implements Runnable {

    public void setToSearch(String toSearch) {
        this.toSearch = toSearch;
    }

    String toSearch;
    List<Place> results;


    public void doSearch(String place){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/geocode?q="+place+
                        "&locale=ru&limit=50&key=ebd26eb5-65af-4ff9-a45a-c4acef8704ab")
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200){
                String content = getJSONFromResponse(response);
                parseJSON(content);
            } else {
                System.out.println("error while connecting");
                results = null;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getJSONFromResponse(Response response){
        try {
            String resp = response.toString();
            int indx = resp.indexOf("https");
            URL url = new URL(resp.substring(indx, resp.length()-1));
            return IOUtils.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String prepareJSON(String content){
        int lb = content.indexOf('[');
        int rb = content.lastIndexOf(']');
        return content.substring(lb, rb+1);
    }

    private void parseJSON(String content) {
        Gson gson = new Gson();
        String prepared = prepareJSON(content);
        Type listType = new TypeToken<List<Place>>() {}.getType();
        results = gson.fromJson(prepared, listType);
    }

    public List<Place> getResults() {
        return results;
    }

    @Override
    public void run() {
        doSearch(toSearch);
    }
}
