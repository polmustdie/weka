package org.example;

import org.example.utils.CsvArffConverter;
import org.example.utils.JsonToCsv;
import weka.clusterers.DBSCAN;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {

        JsonToCsv jsonToCsv = new JsonToCsv();
        jsonToCsv.createCsv("src/main/resources/jsonTest.json");
        CsvArffConverter csvArffConverter = new CsvArffConverter();
        csvArffConverter.convertCsvToArff("src/main/resources/dataFromCsv.csv","src/main/resources/dataArff.arff");

        HilOut gb = new HilOut("dataArff.arff");
        gb.showResults();
    }
}