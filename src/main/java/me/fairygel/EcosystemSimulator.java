package me.fairygel;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EcosystemSimulator {
    private final Set<Ecosystem> ecosystems = new HashSet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public void start() {
        Set<File> ecosystemFiles = getEcosystemFiles();

        for (File file : ecosystemFiles) {
            Ecosystem ecosystem = objectMapper.readValue(file, Ecosystem.class);
            ecosystems.add(ecosystem);
        }
    }

    private Set<File> getEcosystemFiles() {
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

    public String getEcosystems() {
        return ecosystems.stream()
                .map(ecosystem -> ecosystem.getId() + ". " + ecosystem.getName())
                .collect(Collectors.joining("\n"));
    }

    public String createEcosystem() {
        return "";
    }

    public String deleteEcosystem() {
        return "";
    }

    public String loadEcosystem(long id) {
        return ecosystems.stream()
                .filter(ecosystem -> ecosystem.getId() == id)
                .findAny()
                .map(Ecosystem::toString)
                .orElse("Ecosystem not found");
    }

    public String editEcosystem() {
        return "";
    }
}
