package com.saucedemo.automation.getpageobjects;

import com.saucedemo.automation.utils.LayoutValidation;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import static com.saucedemo.automation.getpageobjects.ObjectFileReader.getElementFromFile;

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


protected WebElement element(String elementToken){
        return element(elementToken,"");
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
        } catch(NoSuchElementException exep){
            throw new NoSuchElementException("Failed Element '" +elementToken+ this.pageName);
        }


        return elem;
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
