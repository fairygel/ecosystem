package me.fairygel.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Soil {
    private int humidity;
    private int area;

    @Override
    public String toString() {
        return String.format("soil: humidity = %s, area: %s", humidity, area);
    }
}
