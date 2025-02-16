package com.saucedemo.automation.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SeleniumWait {

    WebDriver driver;
    WebDriverWait wait;
    long timeout;
    public SeleniumWait(WebDriver driver, long timeout){
        this.driver=driver;
        this.wait=new WebDriverWait(driver, Duration.ofSeconds(timeout));
        this.timeout=timeout;
    }

    public WebElement getWhenVisible(By locator){
        WebElement element;
        element= wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return element;
    }
    public WebElement getWhenClickable(By locator){
        WebElement element;
        element= wait.until(ExpectedConditions.elementToBeClickable(locator));
        return element;
    }
    public boolean waitForPageTitleToBeExact(String expectedPagetitle){
        WebElement element;
        return wait.until(ExpectedConditions.titleIs(expectedPagetitle))!=null;
    }
    public boolean waitForPageTitleContains(String expectedPagetitle){
        return wait.until(ExpectedConditions.titleContains(expectedPagetitle))!=null;
    }

    public WebElement waitForElementToBeVisible(WebElement element){
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    public boolean waitUntilNewWindowIsOpen(int numOfWindows){
        return wait.until(ExpectedConditions.numberOfWindowsToBe(numOfWindows));
    }
    public void waitForFrameToBeAvailableAndSwitchToIt(By locator){
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
     }
   public  List<WebElement>  waitForElementsToBeVisible(List<WebElement> elements){
        return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public boolean waitForElementToBeVisible(By locator){
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    public WebElement waitElementToBeClickable(WebElement element){
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void clickWhenReady(By locator){
        WebElement element=wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.click();
    }

    public void waitForPageTitleToAppearCompletely(String txtPageTitle){
        wait.until(ExpectedConditions.titleIs(txtPageTitle));
    }

    public void waitForMsgToastToDisappear(WebElement element){
        int i= 0;
        resetImplicitTimeout(2);
        try{
            while(element.isDisplayed() && i<=timeout){
                hardWait(1);
                i++;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        resetImplicitTimeout(timeout);
    }

    public void resetImplicitTimeout(long newTimeOut){
        try{
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void waitForPageToLoadCompletely(){
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("/*")));
    }

    public void hardWait(int seconds){
        try{
            Thread.sleep(seconds*1000);
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
    }

    public long getTimeout(){
        return timeout;
    }


    public void waitForElementToAppear(WebElement element){
        int i=0;
        System.out.println("waiting for element to appear");
        System.out.println("element.isDisplayed()=="+ element.isDisplayed());
        resetImplicitTimeout(2);
        try{
            System.out.println("try");
            while(!(element.isDisplayed()&& i<=timeout)){
                System.out.println("while");
                hardWait(1);
                i++;
                System.out.println(i+"seconds");
            }
        }catch (StaleElementReferenceException e){
            System.out.println("exception thrown");
        }
        System.out.println("exiting loop");
        resetImplicitTimeout(timeout);
        System.out.println("exiting function");
    }

}

