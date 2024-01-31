package org.evgen.interfaces;

import org.evgen.model.weather.Weather;

public interface IWeatherSearch {
    Weather getWeather(String lat, String lng);
}
