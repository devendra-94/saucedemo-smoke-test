package com.saucedemo.automation.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

public class ConfigPropertyReader {

   private static final String defaultConfigFile="./Config.properties";

   public ConfigPropertyReader(){

   }

   public static String getProperty(String propFile, String Property)
   {
       try{
           Properties prop= ResourceLoader.loadProperties(propFile);
           return prop.getProperty(Property);
       } catch(Exception ex){
           ex.printStackTrace();
           return null;
       }
   }

   public static String getProperty(String property){
       return getProperty(defaultConfigFile,property);
   }

    public static HashMap<String, String> readAllPropertyValuesFromConfigFile() {
        HashMap<String, String> myMap = new HashMap<String,String>();
        Properties prop;

        try {
            // Load properties from the file
            prop = ResourceLoader.loadProperties(defaultConfigFile);

            // Iterate over the entries in the properties file
            for (final Entry<Object, Object> entry : prop.entrySet()) {
                // Add each key-value pair to the map
                myMap.put((String) entry.getKey(),(String) entry.getValue());
            }

            return myMap;
        } catch (IOException e) {
            // Log the error or handle it accordingly
            System.err.println("Error loading properties file: " + e.getMessage());
            return null;  // Return null in case of error
        }
        finally {
            // Optional: Log or perform clean-up (currently nothing is being done here)
            // System.out.println("Finished reading properties.");
        }
    }

}
