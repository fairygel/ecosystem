package me.fairygel.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;
import me.fairygel.entity.organism.Organism;
import me.fairygel.manager.OrganismManager;

public class OrganismDeserializer extends JsonDeserializer<Organism> {

    @Override
    @SneakyThrows
    public Organism deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        long id = node.get("id").asLong();
        String type = node.get("type").asText();
        int population = node.get("population").asInt();

        Organism organism = OrganismManager.readOrganismByIdAndType(id, type);

        if (organism != null) {
            organism.setPopulation(population);
        } else {
            return null;
        }

        return organism;
    }
}
