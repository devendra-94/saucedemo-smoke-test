package com.saucedemo.automation.utils;

import com.saucedemo.automation.TestSessionInitiator;
import org.yaml.snakeyaml.Yaml;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;

public class YamlReader {

    private static String yamlFilePath = null;

    // Set the YAML file path based on the "tier" value
    public static String setYamlFilePath() {
        String tier = String.valueOf(TestSessionInitiator.configSettings.get("tier"));

        switch (tier.toLowerCase()) {
            case "dev":
                yamlFilePath = "src/test/resources/testdata/DEV_TestData.yml";
                break;
            case "qa":
                yamlFilePath = "src/test/resources/testdata/QA_TestData.yml";
                break;
            case "stage":
                yamlFilePath = "src/test/resources/testdata/STAGE_TestData.yml";
                break;
            case "prod":
                yamlFilePath = "src/test/resources/testdata/PROD_TestData.yml";
                break;
            default:
                System.out.println("ERROR: Incorrect tier provided in the configuration.");
                return null; // Return null if tier is incorrect
        }

        System.out.println("Yaml file path: " + yamlFilePath);

        try {
            // Check if the file exists
            new FileReader(yamlFilePath);
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: YAML file not found at: " + yamlFilePath);
            return null; // Return null if the file is not found
        }

        return yamlFilePath;
    }

    // Get data as String from the YAML file
    public static String getData(String token) {
        return getYamlValues(token).toString();
    }

    // Get a value from the YAML file
    public static String getYamlValue(String token) {
        try {
            return getValue(token);
        } catch (FileNotFoundException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        return null;
    }

    // Parse the YAML file and retrieve the values based on the token
    public static Map<String, Object> getYamlValues(String token) {
        Reader doc;
        try {
            doc = new FileReader(yamlFilePath);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found or invalid path.");
            return null;
        }

        Yaml yaml = new Yaml();
        // Load the YAML document into a Map
        Map<String, Object> object = (Map<String, Object>) yaml.load(doc);

        if (object == null) {
            System.out.println("ERROR: Failed to load YAML content.");
            return null;
        }

        return parseMap(object, token + ".");
    }

    // Retrieve the value for a token in the YAML file
    private static String getValue(String token) throws FileNotFoundException {
        Reader doc = new FileReader(yamlFilePath);
        Yaml yaml = new Yaml();
        Map<String, Object> object = (Map<String, Object>) yaml.load(doc);

        if (object == null) {
            System.out.println("ERROR: Failed to load YAML content.");
            return null;
        }

        return getMapValue(object, token);
    }

    // Retrieve the value from a Map for a given token
    public static String getMapValue(Map<String, Object> object, String token) {
        String[] st = token.split("\\.");
        Map<String, Object> parsedMap = parseMap(object, token);

        // Return the last key's value as a String
        Object value = parsedMap.get(st[st.length - 1]);
        return value != null ? value.toString() : null;
    }

    // Parse the map based on the token (i.e., traverse nested maps)
    private static Map<String, Object> parseMap(Map<String, Object> object, String token) {
        if (token.contains(".")) {
            String[] st = token.split("\\.");
            object = parseMap((Map<String, Object>) object.get(st[0]), token.replace(st[0] + ".", ""));
        }
        return object;
    }
}
