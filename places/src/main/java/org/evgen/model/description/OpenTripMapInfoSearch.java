package org.evgen.model.description;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.apache.commons.io.IOUtils;
import org.evgen.interfaces.IDescriptionSearch;
import org.evgen.model.int_places.IntPlace;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class OpenTripMapInfoSearch implements IDescriptionSearch {

    @Override
    public String getDescription(IntPlace place) {
        String urlAPI = "http://api.opentripmap.com/0.1/ru/places/xid/"+place.xid()
                +"?apikey=5ae2e3f221c38a28845f05b65ae560f39d9a453063f501bb19614a9b";
        String content = null;
        try {
            URL url = new URL(urlAPI);
            content = IOUtils.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("failed to connect infoFinder");
        }
        return parseJson(content);
    }

    private String parseJson(String content) {
        JsonObject obj = JsonParser.parseString(content).getAsJsonObject();

        JsonPrimitive text;
        JsonObject info = obj.getAsJsonObject("info");
        if (info != null) {
            text = info.getAsJsonPrimitive("descr");
            return text.getAsString();
        }
        info = obj.getAsJsonObject("wikipedia_extracts");
        if (info != null) {
            text = info.getAsJsonPrimitive("html");
            return text.getAsString();
        }
        return "Об этом месте нет данных :с";
    }
}
