package me.fairygel.entity.organism;

public class Plant extends Organism {
    public static Plant parse(String str) {
        String[] args = str.split("\t");

        if (args.length != 3) return null;

        Plant plant = new Plant();

        plant.setId(Integer.parseInt(args[0]));
        plant.setName(args[1]);
        plant.setEnergy(Integer.parseInt(args[2]));

        return plant;
    }
}
