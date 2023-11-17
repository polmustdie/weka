package org.example.utils;

import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CsvArffConverter

{
    public static void convertCsvToArff(String sourcepath,String destpath) throws Exception
    {
        // load CSV
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(sourcepath));
        Instances dataSet = loader.getDataSet();
        // save ARFF
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(destpath))) {
            //BufferedWriter writer = new BufferedWriter(new FileWriter(destpath));
            writer.write(dataSet.toString());
            writer.flush();
//            writer.close();
        }

    }
//    public static void main(String args[]) throws Exception
//    {
//        Convert("BSEIT.csv", "test.arff");
//    }
}
