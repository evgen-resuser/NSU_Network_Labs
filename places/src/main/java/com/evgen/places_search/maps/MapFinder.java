package com.evgen.places_search.maps;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class MapFinder implements Runnable {


    private String[] coords = new String[]{"55.7505412", "37.6174782"};

    public BufferedImage getMap() {
        return map;
    }

    private BufferedImage map;

    public void setCoords(String[] coords) {
        this.coords = coords;
    }

    //lat long

    @Override
    public void run() {
        doSearch();
    }

    private void doSearch(){
        String urlAPI;
        urlAPI = "https://www.mapquestapi.com/staticmap/v5/map?key=S96jSuwfmPFvfEIEjzGLpe2icHwA6fHz&locations=" +
                coords[0] + "," + coords[1] +
                "|via-FF0000&size=400,100@2x";
        try {
            map = ImageIO.read(new URL(urlAPI));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
