package me.fairygel.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.fairygel.entity.Ecosystem;
import me.fairygel.entity.Soil;
import me.fairygel.entity.Weather;
import me.fairygel.entity.organism.Organism;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EcosystemManager {
    private static final String ECOSYSTEMS_FOLDER = "./simulations/";
    private static final String JSON = ".json";

    private final Map<Long, Ecosystem> ecosystems = new HashMap<>();
    private final Map<Long, File> ecosystemFiles = new HashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();

    public EcosystemManager() {
        readEcosystems();
    }

    private void readEcosystems() {
        Set<File> jsonFiles = getJsonFiles();

        for (File file : jsonFiles) {
            try {
                Ecosystem ecosystem = mapper.readValue(file, Ecosystem.class);

                if (ecosystem.isDeleted()) continue;

                ecosystems.put(ecosystem.getId(), ecosystem);
                ecosystemFiles.put(ecosystem.getId(), file);
            } catch (IOException e) {
                System.out.println("can't read file: " + file.getName());
                System.out.println(e.getMessage());
            }
        }
    }

    // --------------------------CRUD OPERATIONS--------------------------
    @SneakyThrows
    public void createEcosystem(String name, Weather weather, Soil soil) {
        Ecosystem ecosystem = new Ecosystem();

        ecosystem.setId(getLastId() + 1);
        ecosystem.setSoil(soil);
        ecosystem.setCurrentWeather(weather);
        ecosystem.setName(name);

        ecosystems.put(ecosystem.getId(), ecosystem);
        ecosystemFiles.put(ecosystem.getId(), new File(ECOSYSTEMS_FOLDER + ecosystem.getName() + JSON));
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(ECOSYSTEMS_FOLDER + name + JSON), ecosystem);
    }

    public Ecosystem readEcosystemById(long id) {
        return ecosystems.get(id);
    }

    @SneakyThrows
    public void updateEcosystem(long id, String name, Weather weather, Soil soil) {
        Ecosystem ecosystem = readEcosystemById(id);
        File ecosystemFile = ecosystemFiles.get(id);

        ecosystem.setName(name);
        ecosystem.setCurrentWeather(weather);
        ecosystem.setSoil(soil);

        mapper.writerWithDefaultPrettyPrinter().writeValue(ecosystemFile, ecosystem);
    }

    @SneakyThrows
    public void deleteEcosystem(long id) {
        Ecosystem ecosystem = readEcosystemById(id);
        File ecosystemFile = ecosystemFiles.get(id);

        ecosystem.setDeleted(true);
        ecosystemFiles.remove(id);
        ecosystems.remove(id);

        mapper.writerWithDefaultPrettyPrinter().writeValue(ecosystemFile, ecosystem);
    }

    // returns list of ecosystems in format 'id. name\n'
    public String getEcosystems() {
        StringBuilder sb = new StringBuilder();

        for (var pair: ecosystems.entrySet()) {
            sb.append(pair.getKey())
                    .append(". ")
                    .append(pair.getValue().getName())
                    .append('\n');
        }

        return sb.toString();
    }

    private long getLastId() {
        return ecosystems.keySet().stream().max(Long::compareTo).orElse(-1L);
    }
    // --------------------------FILE OPERATIONS METHODS-------------------------------------

    private Set<File> getJsonFiles() {
        File folderFile = new File(ECOSYSTEMS_FOLDER);

        // there is no folder
        if (!folderFile.exists()) {
            if (folderFile.mkdirs()) return Set.of();
            else throw new IllegalStateException("Could not create folder 'simulations'");
        }

        // folder is empty
        if (Objects.requireNonNull(folderFile.listFiles()).length == 0) {
            return Set.of();
        }

        // return all files, that are in simulations folder
        return Stream.of(Objects.requireNonNull(new File(ECOSYSTEMS_FOLDER).listFiles()))
                .filter(file -> !file.isDirectory())
                .filter(file -> file.getName().endsWith(JSON))
                .collect(Collectors.toSet());
    }

    @SneakyThrows
    public void addOrganismToEcosystem(long id, Organism organism) {
        Ecosystem ecosystem = ecosystems.get(id);
        File ecosystemFile = ecosystemFiles.get(id);

        ecosystem.addOrganism(organism);

        mapper.writerWithDefaultPrettyPrinter().writeValue(ecosystemFile, ecosystem);
    }
}
