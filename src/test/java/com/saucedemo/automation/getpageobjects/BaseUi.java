package com.saucedemo.automation.getpageobjects;

import com.saucedemo.automation.utils.SeleniumWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import static com.saucedemo.automation.utils.ConfigPropertyReader.getProperty;


public class BaseUi {

    WebDriver driver;
    protected SeleniumWait wait;
    private int demoWaitSeconds = 0;
    private String pageName;
    protected BaseUi(WebDriver driver, String pageName){
        PageFactory.initElements(driver,this);
        this.driver=driver;
        this.pageName=pageName;
        this.wait=new SeleniumWait(driver,Integer.parseInt(getProperty("Config.properties","timeout")));
    }

    protected void clickUsingJS(WebElement element){
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();",element);
        wait.hardWait(1);
    }
    protected void click(WebElement element){
        try{
            wait.waitForElementToBeVisible(element);
            clickUsingJS(element);
            hardWaitForDemo();
        } catch(StaleElementReferenceException ex1){
            wait.hardWait(3);
            wait.waitForElementToBeVisible(element);
            clickUsingJS(element);
            System.out.println("clicked element afetr stale element refrence exception" +element);
        }catch(Exception ex2){
            System.out.println(ex2.getMessage());
        }

            }

    public void hardWaitForDemo(){
        hardWaitForDemo(getDemoWaitSeconds());
    }
    public void hardWaitForDemo(int seconds){
        if(getDemoWaitSeconds()!=0)
            wait.hardWait(seconds);
    }
    public int getDemoWaitSeconds(){
        return demoWaitSeconds;
    }

}
