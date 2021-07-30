package com.rosewine.awsimageupload.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * I introduce this class to keep my "accessKey.csv" file outside of my project -
 * and so I don't need to hardcode my AWS access-&-secret keys inside my AmazonConfig.java class.
 * Am sure this has been done a zillion times in the past by others: trivial stuff.
 */
public class CsvFile {

    static final String DEFAULT_DELIMITER = ",";

    /**
     * 2D matrix: For each line: give the value of each field.
     */
    private ArrayList<String[]> csvLines = new ArrayList<String[]>();

    public CsvFile(String filePath) throws IOException {
        this(filePath, DEFAULT_DELIMITER);
    }

    /**
     *
     * @param filePath
     * @throws FileNotFoundException - if file doesn't exist in 'FileReader fr = new FileReader(filePath)'
     * @throws IOException - if there is an IO exception in 'while (br.ready())'
     */
    public CsvFile(String filePath, String delimiter) throws /*FileNotFoundException,*/ IOException {

        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);

        while (br.ready()) {
            String line = br.readLine();
            String[] lineFields = line.split(delimiter);
            csvLines.add(lineFields);
        }
    }

    public ArrayList<String[]> getCsvLines() {
        return csvLines;
    }

    /**
     * "main" here is just for primitive testing.
     * @param args
     */
    public static void main(String[] args) {

        String filePath;
        if (args.length == 0)
            filePath = "c:/temp/accessKeys.csv";
        else
            filePath = args[0];

        CsvFile csvFile;
        try {
            csvFile = new CsvFile(filePath);
        } catch(Exception e) {
            System.out.println(e);
            return;
        }

        ArrayList<String[]> csvLines = csvFile.getCsvLines();

        for (int lineNum = 0; lineNum < csvLines.size(); ++lineNum) {
            String[] line = csvLines.get(lineNum);
            for (int colNum = 0; colNum < line.length; ++colNum) {
                String field = line[colNum];
                System.out.print(field + " ");
            }
            System.out.println("");
        }
    }

}
