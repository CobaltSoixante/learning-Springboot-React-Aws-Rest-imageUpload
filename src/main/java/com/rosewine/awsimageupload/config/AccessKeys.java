package com.rosewine.awsimageupload.config;

import java.util.ArrayList;

public class AccessKeys {

    private CsvFile accessKeysFile;
    private ArrayList<String[]> accessKeysFileFields;

    public AccessKeys(String filePath) {
        try {
            this.accessKeysFile = new CsvFile(filePath);
            this.accessKeysFileFields = this.accessKeysFile.getCsvLines();
        } catch(Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    public String getAccessKey() {
        return accessKeysFileFields.get(1)[0];
    }

    public String getSecretKey() {
        return accessKeysFileFields.get(1)[1];
    }

    public static void main(String[] args) {

        AccessKeys accessKeys = new AccessKeys("C:/temp/accessKeys.csv");
        System.out.println(accessKeys.getAccessKey());
        System.out.println(accessKeys.getSecretKey());
    }

}
