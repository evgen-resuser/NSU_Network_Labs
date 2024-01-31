package org.evgen.model.weather;

public record Weather(String description, String temp, String feelsLike,
                      String tempMin, String tempMax, String pressure,
                      String humidity, String windSpeed) {
    @Override
    public String toString() {
        return String.format("<div style=\"font-family: Arial;\"><b>%s</b><br>Температура: %sС<br>Ощущается как %sС<br>" +
                        "Мин/Макс: %sC/%sС<br>Давление: %s мм.рт.ст<br>Влажность: %s%%<br>Скорость ветра: %s м/с</div>",
                description, temp, feelsLike, tempMin, tempMax, pressure, humidity, windSpeed);
    }
}
