package com.saucedemo.automation.getpageobjects;

import com.saucedemo.automation.utils.LayoutValidation;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static com.saucedemo.automation.getpageobjects.ObjectFileReader.getElementFromFile;
import static org.testng.Assert.*;

public class GetPage extends BaseUi {


    protected WebDriver driver;
    String pageName;
    LayoutValidation layouttest;


    public GetPage(WebDriver driver, String pageName) {
        super(driver, pageName);
        this.driver=driver;
        this.pageName=pageName;
        layouttest=new LayoutValidation(driver, pageName);
    }

public void testPageLayout(List<String> tagsToBeTested){
        layouttest.checkLayout(tagsToBeTested);
}

protected WebElement getElementWhenVisible(String elementToken){
        return getElementWhenVisible(elementToken,"");
}

protected void switchToNestedFrames(String frameNames){
        switchToDefaultContent();
        String[] frameIdentifiers=frameNames.split(":");
        for(String frameId: frameIdentifiers){
            wait.waitForFrameToBeAvailableAndSwitchToIt(getLocator(frameId.trim()));
        }
}

    private By getLocator(String elementToken) {
        return getLocator(elementToken,"");
    }

    protected  WebElement getElementWhenVisible(String elementToken, String replacement)throws NoSuchElementException{
        WebElement foundElement= null;
        try{
            By elementLocator= getLocator(elementToken,replacement);
            WebElement webelement= driver.findElement(elementLocator);
            foundElement=wait.waitForElementToBeVisible(webelement);
        }catch(NoSuchElementException exec){
            fail("[ASSERT FAILED]: Element" + elementToken+" not found on the webpage !!!!");
        }catch(NullPointerException npe){
            logMessage("[UNHANDLED EXCEPTION]:  "+npe.getLocalizedMessage());
        }
return foundElement;
    }

protected WebElement element(String elementToken){
        return element(elementToken,"");
}
protected WebElement element(String elementToken,String replacement1,String replacement2) throws NoSuchElementException{
        WebElement elem= null;
        try{
            elem= wait.waitForElementToBeVisible(driver.findElement(getLocator(elementToken, replacement1, replacement2)));
        }catch(StaleElementReferenceException ex1){
            wait.hardWait(1);
            elem=wait.waitForElementToBeVisible(driver.findElement(getLocator(elementToken,replacement1,replacement2)));
        logMessage("Find Element "+ elementToken+" after catching stale element exception ");

        } catch(NoSuchElementException excp){
            fail("Failed: Element "+ elementToken+" not found on the "+this.pageName+" !!!");
        }
        return elem;
}


protected By getLocator(String elementToken, String replacement){
        String[] locator=getElementFromFile(this.pageName,elementToken);
        locator[2]=locator[2].replaceAll("\\$\\{.+\\}",replacement);
        return getBy(locator[1].trim(),locator[2].trim());
}

protected WebElement element(String elementToken, String replacement) throws NoSuchElementException {
        WebElement elem=null;
        By locator=getLocator(elementToken,replacement);

        try{
            elem=new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch(StaleElementReferenceException ex1){
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
            elem= new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            logMessage("Recovered StaleElementRefrenceException for Element: "+ elementToken);
        } catch(NoSuchElementException exep){
            throw new NoSuchElementException("Failed: Element '" +elementToken+ this.pageName);
        }


        return elem;
}

protected List<WebElement> elements(String elementToken, String replacement){
        return wait.waitForElementsToBeVisible(driver.findElements(getLocator(elementToken,replacement)));
}

    protected List<WebElement> elements(String elementToken, String replacement1, String replacement2){
        return driver.findElements(getLocator(elementToken,replacement1,replacement2));
    }

protected List<WebElement> elements(String elementToken){
        hardWaitForDemo(2);
        return elements(elementToken,"");
}

protected void waitForElementToAppear(String elementToken){
        wait.waitForElementToAppear(element(elementToken));
}

protected void _waitForElementToDisappear(String elementToken, String replacement){
        int i=0;
        long initTimeout=wait.getTimeout();
        wait.resetImplicitTimeout(20);
        int count;
        while (i<=20){
            if(replacement.isEmpty())
                count= elements(elementToken).size();
            else
                count = elements(elementToken,replacement).size();
            if(count ==0)
                break;
            i+=2;
        }
        wait.resetImplicitTimeout(initTimeout);
}
protected void waitForElementToDisappear(String elementToken){
        _waitForElementToDisappear(elementToken,"");
}
    protected void waitForElementToDisappear(String elementToken,String replacement){
        _waitForElementToDisappear(elementToken,replacement);
    }
protected void isStringMatching(String actual,String expected){
    logMessage("Comparing 2 strings");
    logMessage("Expected STRING : "+ expected);
    logMessage("ACTUAL STRING : " + actual);
    assertEquals(actual,expected,"The strings does not match!!!");
    logMessage("String compare Assertion passed");
}

protected boolean isElementDisplayed(String elementName,String elementTextReplace){
        wait.waitForElementToBeVisible(element(elementName,elementTextReplace));
        boolean result = element(elementName,elementTextReplace).isDisplayed();
        assertTrue(result, "ASSERTION FAILED: element '"+elementName+"with text"+ elementTextReplace+"' is not displayed.");
        logMessage("ASSERTION PASSED: element"+ elementName+ " with text "+ elementTextReplace+" is displayed.");
        return true;

    }
    protected boolean isElementNotDisplayed(String elementName,String elementTextReplace){
        wait.waitForElementToBeVisible(element(elementName,elementTextReplace));
        boolean result = element(elementName,elementTextReplace).isDisplayed();
        assertTrue(!result, "ASSERTION FAILED: element '"+elementName+"with text"+ elementTextReplace+"' is not displayed.");
        logMessage("ASSERTION PASSED: element"+ elementName+ " with text "+ elementTextReplace+" is not displayed.");
        return true;

    }
protected WebElement nthElement(String elementToken, int index){
        hardWaitForDemo(2);
        return element(elementToken,"",index);
}

protected void verifyElementText(String elementName, String expectedText){
        wait.waitForElementToBeVisible(element(elementName));
        assertEquals(element(elementName).getText().trim(),expectedText,"ASSERTION FAILED: element '"+ elementName+"'Text is not expected: ");
logMessage("ASSERTION PASSED element '"+elementName+"' Text is not as expected: ");

    }

    protected WebElement element(String elementToken, String replacement,int index)throws NoSuchElementException{
        WebElement elem=null;
        try{
            elem=wait.waitForElementToBeVisible(driver.findElement(getLocator(elementToken,replacement,index)));
        }catch(StaleElementReferenceException ex1){
            wait.hardWait(1);
            elem=wait.waitForElementToBeVisible(driver.findElement(getLocator(elementToken,replacement,index)));
       logMessage("Find Element "+ elementToken+" after catching stale element exception");
        }catch(NoSuchElementException excp){
            fail("Failed: Element '"+elementToken+"' not found on the "+this.pageName+" !!!");
        } catch(TimeoutException excp){
            fail("Failed: Timeout while waiting for element '"+elementToken+"' not found on the "+this.pageName+" !!!");
        }
        return elem;
    }

    protected By getLocator(String elementToken, String replacement1, String replacement2){
        String[] locator=getElementFromFile(this.pageName,elementToken);
        locator[2]=locator[2].replaceAll("\\$\\{.+\\}",replacement1);
        locator[2]=locator[2].replaceAll("\\$\\{.+\\}",replacement2);
        return getBy(locator[1].trim(),locator[2].trim());
    }

    protected By getLocator(String elementToken, String replacement, int index){
        String[] locator=getElementFromFile(this.pageName,elementToken);
        if(index<0){
            logMessage("elementToken= "+ elementToken+" replacement="+ replacement+"; index="+index);
            logMessage("locator[1]= "+ locator[1]+"; locator[2]="+ locator[2]);
        }
        if(index>0&&replacement.length()==0)
            locator[2]=locator[2].replace("n","("+index+")");
        else
            locator[2]=locator[2].replaceAll("\\$\\{.+\\}",replacement);
        if(index<0)
            logMessage("locator[2]="+ locator[2]);
        return getBy(locator[1].trim(),locator[2].trim());
    }


    private By getBy(String locatorType,String locatorValue){
  switch(Locators.valueOf(locatorType)){
      case id:
          return By.id(locatorValue);
      case xpath:
          return  By.xpath(locatorValue);
      case css:
          return By.cssSelector(locatorValue);
      case name:
          return By.name(locatorValue);
      case classname:
          return By.className(locatorValue);
      case linktext:
          return By.linkText(locatorValue);
      case partialLinkText:
          return By.partialLinkText(locatorValue);
      default:
          return By.id(locatorValue);


  }

}

}
