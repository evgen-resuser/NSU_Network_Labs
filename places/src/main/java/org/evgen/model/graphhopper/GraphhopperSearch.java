package org.evgen.model.graphhopper;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.evgen.interfaces.IPlacesSearch;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class GraphhopperSearch implements IPlacesSearch {

    @Override
    public List<Place> getResponse(String name) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graphhopper.com/api/1/geocode?q="+name+
                        "&locale=ru&limit=50&key=ebd26eb5-65af-4ff9-a45a-c4acef8704ab")
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200){
                assert response.body() != null;
                return parseResponse(response.body().string());
            } else {
                System.out.println("error while connecting");
                return Collections.emptyList();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }

    private List<Place> parseResponse(String response) {

        JsonObject object = JsonParser.parseString(response).getAsJsonObject();
        JsonArray hits = object.get("hits").getAsJsonArray();

        Gson gson = new Gson();
        Type listType = new TypeToken<List<Place>>() {}.getType();

        return gson.fromJson(hits, listType);
    }
}