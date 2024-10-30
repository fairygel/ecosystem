package me.fairygel.entity;

public class Plant extends Organism {
    public static Plant parse(String str) {
        String[] args = str.split("\t");

        Plant plant = new Plant();

        plant.setId(Integer.parseInt(args[0]));
        plant.setName(args[1]);
        plant.setEnergy(Integer.parseInt(args[2]));

        return plant;
    }
}
