package com.evgen.places_search.weather;

public class Weather {

    String description;
    String temp;
    String feelsLike;
    String tempMin;
    String tempMax;
    String pressure;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    String humidity;
    String windSpeed;

    @Override
    public String toString() {
        return String.format("<div style=\"font-family: Arial;\"><b>%s</b><br>Температура: %sС<br>Ощущается как %sС<br>" +
                        "Мин/Макс: %sC/%sС<br>Давление: %s кПа<br>Влажность: %s%%<br>Скорость ветра: %s м/с</div>",
                description, temp, feelsLike, tempMin, tempMax, pressure, humidity, windSpeed);
    }
}
