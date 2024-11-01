package me.fairygel.entity.organism;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import me.fairygel.utils.OrganismDeserializer;

@Setter
@Getter
@JsonDeserialize(using = OrganismDeserializer.class)
public class Organism {
    private long id;
    private String name;
    private int population;
    private int energy;

    @Override
    public String toString() {
        return String.format("%s. %s(population = %s)", id, name, population);
    }

    public String toSaveString() {
        return String.format("%s\t%s\t%s%n", id, name, energy);
    }

    public static Organism parse(String str) {
            String[] args = str.split("\t");

            if (args.length != 3) return null;

            Organism organism = new Organism();

            organism.setId(Integer.parseInt(args[0]));
            organism.setName(args[1]);
            organism.setEnergy(Integer.parseInt(args[2]));

            return organism;
    }
}
