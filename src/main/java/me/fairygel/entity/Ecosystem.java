package me.fairygel.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.fairygel.entity.organism.Organism;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Ecosystem {
    private long id;
    private String name;
    private long passedDays;
    @JsonProperty("isDeleted")
    private boolean isDeleted;

    @JsonIgnore
    private Weather currentWeather;
    @JsonIgnore
    private Soil soil;
    @JsonIgnore
    private List<Organism> organisms;

    @Override
    public String toString() {
        return String.format("%s. %s (passed days: %s)", id, name, passedDays);
    }
}
