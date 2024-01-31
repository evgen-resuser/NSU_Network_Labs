package org.evgen.model.map;

import org.evgen.interfaces.IMapSearch;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class MapQuestSearch implements IMapSearch {
    @Override
    public BufferedImage getMap(String lat, String lng) {
        String url = "https://www.mapquestapi.com/staticmap/v5/map?key=S96jSuwfmPFvfEIEjzGLpe2icHwA6fHz&locations=" +
                lat + "," + lng +
                "|via-FF0000&size=400,100@2x";
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            System.out.println("Map error!");
        }
        return null;
    }
}
