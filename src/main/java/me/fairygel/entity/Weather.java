package me.fairygel.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Weather {
    private WeatherConditions conditions;
    private int temperature;

    @Override
    public String toString() {
        return String.format(
                "weather: temperature = %s, conditions = %s%n", temperature, conditions.name().toLowerCase()
        );
    }
}
