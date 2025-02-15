package com.saucedemo.automation.utils;

import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.saucedemo.automation.utils.ConfigPropertyReader.getProperty;

public class TakeScreenshot {

WebDriver driver;
String testname, testcaseName;
String screenshotPath="/target";


public TakeScreenshot(String testname, WebDriver driver)
{
   this.driver=driver;
   this.testname=testname;
}

public void takeScreenshot(Scenario scenario){
  screenshotPath=(getProperty("screenshot-path")!=null) ? getProperty("screenshot-path") : screenshotPath;
    DateFormat dateFormat= new SimpleDateFormat("yyyy_MM_dd_hh_mm_a");
    Date date= new Date();
    String date_time= dateFormat.format(date);
    File file= new File(
            System.getProperty("user.dir")+File.separator+screenshotPath);
  boolean exists= file.exists();
  if(!exists){
      new File(System.getProperty("user.dir")+File.separator+screenshotPath)
              .mkdir();
  }
  File scrFile= ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
  byte[] screenshot=((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
  scenario.attach(screenshot,"image/png","failed to take screenshot");
  try{
      String saveImgFile=System.getProperty("user.dir")+File.separator+screenshotPath+File.separator+date_time+"_screenshot"+testcaseName+".png";
      FileUtils.copyFile(scrFile,new File(saveImgFile));
  } catch(IOException e){
      System.out.println(e.getMessage());
  }


}

public void takeScreenshotOnException(Scenario scenarioName) throws RuntimeException{

    testcaseName= scenarioName.getName().toString().toUpperCase();
    String takeScreenshot=getProperty("take-screenshot");
    testcaseName =testcaseName.trim();
    if(scenarioName.isFailed()){
        if(takeScreenshot.equalsIgnoreCase("true") || takeScreenshot.equalsIgnoreCase("yes"))
        {
            try{
                if(driver != null){
                    takeScreenshot(scenarioName);
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}

}
