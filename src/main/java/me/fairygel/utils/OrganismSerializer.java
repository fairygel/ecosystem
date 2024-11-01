package me.fairygel.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;
import me.fairygel.entity.organism.Animal;
import me.fairygel.entity.organism.Organism;

public class OrganismSerializer extends JsonSerializer<Organism> {
    @SneakyThrows
    @Override
    public void serialize(Organism organism, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        jsonGenerator.writeStartObject();

        String type = organism instanceof Animal ? "animal": "plant";

        jsonGenerator.writeNumberField("id", organism.getId());
        jsonGenerator.writeStringField("type", type);
        jsonGenerator.writeNumberField("population", organism.getPopulation());

        jsonGenerator.writeEndObject();
    }
}
