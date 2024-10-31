package me.fairygel;

import lombok.SneakyThrows;
import me.fairygel.entity.Animal;
import me.fairygel.entity.Organism;
import me.fairygel.entity.Plant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import java.io.*;


public class OrganismManager {
    private final Map<Long, Animal> animals = new HashMap<>();
    private final Map<Long, Plant> plants = new HashMap<>();

    private final Map<Long, Boolean> modifiedAnimals = new HashMap<>();
    private final Map<Long, Boolean> modifiedPlants = new HashMap<>();

    private BufferedWriter animalWriter;
    private BufferedWriter plantWriter;


    public OrganismManager() {
        readOrganisms();
    }

    public void start() {
        createWriters();
    }

    public String getOrganisms(boolean isAnimal) {
        Map<Long, ? extends Organism> organisms = isAnimal? animals : plants;
        StringBuilder sb = new StringBuilder();

        for (Organism organism : organisms.values()) {
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
    private void endWriting() {
        animalWriter.flush();
        animalWriter.close();

        plantWriter.flush();
        plantWriter.close();
    }

    private void readOrganisms() {
        if (!animals.isEmpty() || !plants.isEmpty()) return;

        try (BufferedReader animalReader = getFileReader("animals.txt");
             BufferedReader plantReader = getFileReader("plants.txt")) {

        String line;

        while ((line = animalReader.readLine()) != null) {
            Animal animal = Animal.parse(line);
            if (animal != null)
                animals.put(animal.getId(), animal);
        }

        while ((line = plantReader.readLine()) != null) {
            Plant plant = Plant.parse(line);
            if (plant != null)
                plants.put(plant.getId(), plant);
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
            organism.setId(getLastId(true)+1);

            animals.put(organism.getId(), (Animal) organism);
        } else {
            organism = new Plant();
            organism.setId(getLastId(false)+1);

            plants.put(organism.getId(), (Plant) organism);
        }

        organism.setEnergy(energy);
        organism.setName(name);

        if (isAnimal) animalWriter.write(organism.toString());
        else plantWriter.write(organism.toString());
    }


    private long getLastId(boolean isAnimal) {
        Map<Long, ? extends Organism> organisms = isAnimal? animals: plants;

        return organisms.keySet().stream().max(Long::compareTo).orElse(-1L);
    }

    @SneakyThrows
    public Organism getOrganismById(long id, boolean isAnimal) {
        if (isAnimal)
            return animals.get(id);
        else
            return plants.get(id);
    }

    public void saveChanges() {
        endWriting();

        saveModifications(modifiedAnimals, "animals", true);
        saveModifications(modifiedPlants, "plants", false);
    }

    @SneakyThrows
    private void saveModifications(Map<Long, Boolean> modified, String filename, boolean isAnimal) {
        if (modified.isEmpty()) return;

        File tempFile = new File(filename + ".tmp");
        File destFile = new File(filename + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(destFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");

                if (parts.length != 3) continue;

                long id = Long.parseLong(parts[0]);

                if (modified.containsKey(id) && Boolean.FALSE.equals(modified.get(id))) {
                    continue;
                }

                if (modified.containsKey(id) && Boolean.TRUE.equals(modified.get(id))) {
                    writer.write(getOrganismById(id, isAnimal).toString());
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

            modified.clear();
        }

        Files.move(tempFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public void updateOrganism(long id, String name, int energy, boolean isAnimal) {
        Organism organism = getOrganismById(id, isAnimal);

        organism.setName(name);
        organism.setEnergy(energy);

        if (isAnimal) modifiedAnimals.put(id, true);
        else modifiedPlants.put(id, true);
    }

    public void deleteOrganism(long id, boolean isAnimal) {
        if (isAnimal) {
            animals.remove(id);
            modifiedAnimals.put(id, false);
        } else {
            plants.remove(id);
            modifiedPlants.put(id, false);
        }
    }
}

