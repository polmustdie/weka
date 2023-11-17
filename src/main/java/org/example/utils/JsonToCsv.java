package org.example.utils;

import com.fasterxml.jackson.core.JsonGenerator.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;
import java.io.IOException;
//"src/main/resources/data.json"
public class JsonToCsv {

    public static void createCsv(String path) throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(new File(path));

        CsvSchema.Builder builder = CsvSchema.builder()
                .addColumn("x")
                .addColumn("y")
                .addColumn("flag");

        CsvSchema csvSchema = builder.build().withHeader();

        CsvMapper csvMapper = new CsvMapper();
        csvMapper.configure(Feature.IGNORE_UNKNOWN, true);
        csvMapper.writerFor(JsonNode.class)
                .with(csvSchema)
                .writeValue(new File("src/main/resources/dataFromCsv.csv"), jsonNode);
    }
}