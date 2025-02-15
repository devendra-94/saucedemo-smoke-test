package com.saucedemo.automation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourceLoader {

private ResourceLoader()
{
}

public static InputStream loadResource(String resourcesName) throws IOException{
    ClassLoader classLoader = ResourceLoader.class.getClassLoader();
    InputStream inputStream=null;
    if(classLoader != null){
        inputStream= classLoader.getResourceAsStream(resourcesName);
    }
    if(inputStream ==null){
        classLoader=ClassLoader.getSystemClassLoader();
        if(classLoader!= null){
            inputStream= classLoader.getResourceAsStream(resourcesName);
        }
    }
    if(inputStream==null){
        File file= new File(resourcesName);
        if(file.exists()){
            inputStream= new FileInputStream(file);
        }
    }

    return inputStream;
}

public static Properties loadProperties(String resourceName) throws IOException
    {
      Properties properties=null;
      InputStream inputStream= null;
      try{
          inputStream =loadResource(resourceName);
          if(inputStream != null){
              properties= new Properties();
              properties.load(inputStream);
          }
      } finally{
          if(inputStream!=null){
              inputStream.close();
          }
      }
        return properties;

    }

}
