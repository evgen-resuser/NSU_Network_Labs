package com.evgen.places_search.interesting;

import com.google.gson.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class InfoFinder implements Runnable{

    private String urlAPI;
    private String xid;
    private String finalInfo;

    public String getInfo() {
        return finalInfo;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    @Override
    public void run() {
        doSearch();
    }

    private void doSearch(){
        urlAPI = "http://api.opentripmap.com/0.1/ru/places/xid/"+xid
                +"?apikey=5ae2e3f221c38a28845f05b65ae560f39d9a453063f501bb19614a9b";
        String content = getJSONFromResponse();
        parseJson(content);
    }

    private String getJSONFromResponse(){
        String content = null;
        try {
            URL url = new URL(urlAPI);
            content = IOUtils.toString(url, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("failed to connect infoFinder");
        }
        return content;
    }

    private void parseJson(String content) {
        JsonObject obj = JsonParser.parseString(content).getAsJsonObject();

        JsonPrimitive text;
        JsonObject info = obj.getAsJsonObject("info");
        if (info != null) {
            text = info.getAsJsonPrimitive("descr");
            finalInfo = text.getAsString();
            return;
        }
        info = obj.getAsJsonObject("wikipedia_extracts");
        if (info != null) {
            text = info.getAsJsonPrimitive("html");
            finalInfo = text.getAsString();
            return;
        }
        finalInfo = "Об этом месте нет данных :с";
    }

}
