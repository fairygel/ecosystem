package me.fairygel.entity;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Organism {
    private int id;
    private String name;
    private int population;
    private int energy;

    @Override
    public String toString() {
        return String.format("%s\t%s\t%s%n", id, name, energy);
    }

    public static Organism parse(String str) {
            String[] args = str.split("\t");

            Organism organism = new Organism();

            organism.setId(Integer.parseInt(args[0]));
            organism.setName(args[1]);
            organism.setEnergy(Integer.parseInt(args[2]));

            return organism;
    }
}
