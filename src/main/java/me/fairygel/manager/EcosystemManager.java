package me.fairygel.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import me.fairygel.entity.Ecosystem;
import me.fairygel.entity.organism.Organism;
import me.fairygel.utils.OrganismDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EcosystemManager {
    private final Map<Long, Ecosystem> ecosystems = new HashMap<>();
    private final Map<Long, File> ecosystemFiles = new HashMap<>();

    private final ObjectMapper mapper = new ObjectMapper();

    public EcosystemManager() {
        SimpleModule module = new SimpleModule();

        OrganismDeserializer organismDeserializer = new OrganismDeserializer();

        module.addDeserializer(Organism.class, organismDeserializer);
        mapper.registerModule(module);

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
    public String createEcosystem() {
        return "";
    }

    public Ecosystem readEcosystemById(long id) {
        return ecosystems.get(id);
    }

    public String updateEcosystem() {
        return "";
    }

    public String deleteEcosystem() {
        return "";
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

    // --------------------------FILE OPERATIONS METHODS-------------------------------------

    private Set<File> getJsonFiles() {
        String folderName = "./simulations/";
        File folderFile = new File(folderName);

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
        return Stream.of(Objects.requireNonNull(new File("./simulations/").listFiles()))
                .filter(file -> !file.isDirectory())
                .filter(file -> file.getName().endsWith(".json"))
                .collect(Collectors.toSet());
    }

}
