package me.fairygel.manager;

import lombok.SneakyThrows;
import me.fairygel.entity.organism.Animal;
import me.fairygel.entity.organism.Organism;
import me.fairygel.entity.organism.Plant;

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
    private boolean isAnimal;

    private static final Map<Long, Animal> animals = new HashMap<>();
    private static final Map<Long, Plant> plants = new HashMap<>();

    // boolean is a flag of updated animal. false = deleted, true = updated
    private final Map<Long, Boolean> modifiedOrganisms = new HashMap<>();

    private BufferedWriter organismWriter;

    public OrganismManager() {
        readOrganisms();
    }

    public void start(boolean isAnimal) {
        this.isAnimal = isAnimal;

        createWriters();
    }

    public boolean hasAnyOrganism() {
        return isAnimal? !animals.isEmpty(): !plants.isEmpty();
    }

    // -----------------CRUD OPERATIONS---------------

    @SneakyThrows
    public void createOrganism(String name, int energy) {
        Organism organism;

        // create organism
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

        // save to file
        organismWriter.write(organism.toSaveString());
    }

    public Organism readOrganismById(long id) {
        return isAnimal? animals.get(id): plants.get(id);
    }

    public static Organism readOrganismByIdAndType(long id, String type) {
        if (type.equalsIgnoreCase("animal")) {
            return animals.get(id);
        } else if (type.equalsIgnoreCase("plant")) {
            return plants.get(id);
        } else {
            throw new IllegalArgumentException("invalid type. Use 'animal' or 'plant'.");
        }
    }

    public void updateOrganism(long id, String name, int energy) {
        Organism organism = readOrganismById(id);

        organism.setName(name);
        organism.setEnergy(energy);

        modifiedOrganisms.put(id, true);
    }

    public void deleteOrganism(long id, boolean isAnimal) {
        if (isAnimal) {
            animals.remove(id);
        } else {
            plants.remove(id);
        }

        modifiedOrganisms.put(id, false);
    }

    // returns list of animals in format 'id. name\n'
    public String getOrganisms() {
        Map<Long, ? extends Organism> organisms = isAnimal? animals : plants;
        StringBuilder sb = new StringBuilder();

        for (Organism organism : organisms.values()) {
            sb.append (
                    String.format("%s. %s, energy = %s%n", organism.getId(), organism.getName(), organism.getEnergy())
            );
        }

        return sb.toString();
    }

    private long getLastId(boolean isAnimal) {
        Map<Long, ? extends Organism> organisms = isAnimal? animals: plants;

        return organisms.keySet().stream().max(Long::compareTo).orElse(-1L);
    }

    // -----------------FILE OPERATIONS METHODS---------------------

    @SneakyThrows
    private void createWriters() {
        String filename = isAnimal? "animals.txt": "plants.txt";

        organismWriter = new BufferedWriter(new FileWriter(filename, true));
    }

    // fills maps with plants and animals
    private void readOrganisms() {
        if (!animals.isEmpty() || !plants.isEmpty()) return;

        try (BufferedReader animalReader = getFileReader("animals.txt");
             BufferedReader plantReader = getFileReader("plants.txt")) {

            String line;

            // fill animals map
            while ((line = animalReader.readLine()) != null) {
                Animal animal = Animal.parse(line);
                if (animal != null)
                    animals.put(animal.getId(), animal);
            }

            // fill plants map
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


    public void saveChanges() {
        endWriting();
        String filenameWithNoExtension = isAnimal? "animals": "plants";

        saveModifications(modifiedOrganisms, filenameWithNoExtension);
    }

    @SneakyThrows
    private void endWriting() {
        organismWriter.flush();
        organismWriter.close();
    }

    // reads line by line file with organisms and replaces MODIFIED organisms in it.
    @SneakyThrows
    private void saveModifications(Map<Long, Boolean> modified, String filenameWithNoExtension) {
        if (modified.isEmpty()) return;

        File tempFile = new File(filenameWithNoExtension + ".tmp");
        File destFile = new File(filenameWithNoExtension + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(destFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");

                if (parts.length != 3) continue;

                long id = Long.parseLong(parts[0]);

                // if organism is in modified map and is deleted then we skip it
                if (modified.containsKey(id) && Boolean.FALSE.equals(modified.get(id))) {
                    continue;
                }

                // if organism is in modified map and is updated then we replace it in file
                if (modified.containsKey(id) && Boolean.TRUE.equals(modified.get(id))) {
                    writer.write(readOrganismById(id).toSaveString());
                } else {
                    writer.write(line); // else we keep previous value
                }
                writer.newLine();
            }

            modified.clear();
        }

        Files.move(tempFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
}