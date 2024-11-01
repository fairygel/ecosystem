package me.fairygel.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.fairygel.entity.organism.Animal;
import me.fairygel.entity.organism.Organism;
import me.fairygel.entity.organism.Plant;
import me.fairygel.utils.OrganismSerializer;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ecosystem {
    private long id;
    private String name;
    private long passedDays = 0;
    @JsonProperty("isDeleted")
    private boolean isDeleted = false;

    private Weather currentWeather;
    private Soil soil;

    @JsonSerialize(contentUsing = OrganismSerializer.class)
    private List<Organism> organisms = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder organismStringBuilder = new StringBuilder();

        for (Organism organism: organisms) {
            if (organism == null) continue;

            if (organism instanceof Animal)
                organismStringBuilder.append(organism).append(" (animal)\n");
            else if (organism instanceof Plant)
                organismStringBuilder.append(organism).append(" (plant)\n");
            else
                organismStringBuilder.append(organism);
        }

        String firstPart = String.format("%s. '%s' ecosystem (%s days passed).%n", id, name, passedDays);
        String secondPart = String.format("%s%s%n-------------ORGANISMS-------------%n%s",
                currentWeather, soil, organismStringBuilder);

        return firstPart + secondPart;
    }
}
