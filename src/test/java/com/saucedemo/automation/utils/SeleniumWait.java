package com.saucedemo.automation.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SeleniumWait {

    WebDriver driver;
    WebDriverWait wait;
    long timeout;
    public SeleniumWait(WebDriver driver, long timeout){
        this.driver=driver;
        this.wait=new WebDriverWait(driver, Duration.ofSeconds(timeout));
        this.timeout=timeout;
    }

    public WebElement waitForElementToBeVisible(WebElement element){
        return wait.until(ExpectedConditions.visibilityOf(element));
    }
    public void hardWait(int seconds){
        try{
            Thread.sleep(seconds*1000);
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }

}
