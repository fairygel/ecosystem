package me.fairygel;

import lombok.SneakyThrows;
import me.fairygel.entity.Animal;
import me.fairygel.entity.Organism;
import me.fairygel.entity.Plant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;

import java.io.*;


public class OrganismManager {
    private final List<Animal> animals = new ArrayList<>();
    private final List<Plant> plants = new ArrayList<>();

    private BufferedWriter animalWriter;
    private BufferedWriter plantWriter;


    public OrganismManager() {
        readOrganisms();

        createWriters();
    }

    public String getOrganisms(boolean isAnimal) {
        List<? extends Organism> organisms = isAnimal? animals : plants;
        StringBuilder sb = new StringBuilder();

        for (Organism organism : organisms) {
            sb.append(organism.getId())
                    .append(". ")
                    .append(organism.getName())
                    .append('\n');
        }

        return sb.toString();
    }

    @SneakyThrows
    private void createWriters() {
        animalWriter = new BufferedWriter(new FileWriter("animals.txt", true));
        plantWriter = new BufferedWriter(new FileWriter("plants.txt", true));
    }

    @SneakyThrows
    public void end() {
        animalWriter.flush();
        plantWriter.flush();

        animalWriter.close();
        plantWriter.close();
    }

    private void readOrganisms() {
        if (!animals.isEmpty() || !plants.isEmpty()) return;

        try (BufferedReader animalReader = getFileReader("animals.txt");
             BufferedReader plantReader = getFileReader("plants.txt")) {

        String line;

        while ((line = animalReader.readLine()) != null) {
            animals.add(Animal.parse(line));
        }

        while ((line = plantReader.readLine()) != null) {
            plants.add(Plant.parse(line));
        }

        } catch (IOException e) {
            System.err.println("Ошибка при чтении файлов: " + e.getMessage());
        }
    }

    @SneakyThrows
    private BufferedReader getFileReader(String filename) {
        File file = new File(filename);

        if (!file.exists() && !file.createNewFile()) {
            throw new FileSystemException("Could not create file '" + filename + "'");
        }

        return new BufferedReader(new FileReader(file));
    }

    @SneakyThrows
    public void createOrganism(String name, int energy, boolean isAnimal) {
        Organism organism;

        if (isAnimal) {
            organism = new Animal();
            animals.add((Animal) organism);
        } else {
            organism = new Plant();
            plants.add((Plant) organism);
        }

        organism.setId(getLastId(isAnimal)+1);
        organism.setEnergy(energy);
        organism.setName(name);

        if (isAnimal) animalWriter.write(organism.toString());
        else plantWriter.write(organism.toString());
    }


    private int getLastId(boolean isAnimal) {
        List<? extends Organism> organisms = isAnimal? animals: plants;

        return organisms.stream()
               .mapToInt(Organism::getId)
               .max()
               .orElse(0);
    }

    @SneakyThrows
    public Organism getByIdOrganism(long id, boolean isAnimal) {
        List<? extends Organism> organisms = isAnimal? animals : plants;

        return organisms.stream()
                .filter(organism -> organism.getId() == id)
                .findAny()
                .orElseThrow();
    }
}

