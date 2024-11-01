package me.fairygel.entity.organism;

public class Animal extends Organism {
    public static Animal parse(String str) {
        String[] args = str.split("\t");

        if (args.length != 3) return null;

        Animal animal = new Animal();

        animal.setId(Integer.parseInt(args[0]));
        animal.setName(args[1]);
        animal.setEnergy(Integer.parseInt(args[2]));

        return animal;
    }
}
