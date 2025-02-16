package com.saucedemo.automation.getpageobjects;

import com.saucedemo.automation.TestSessionInitiator;
import com.saucedemo.automation.utils.SeleniumWait;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import static com.saucedemo.automation.getpageobjects.ObjectFileReader.getPageTitleFromFile;
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
protected String getPageTitle(){
        return driver.getTitle();
}
protected String getCurrentURL(){
        return driver.getCurrentUrl();
}
protected void logMessage(String message){
        Reporter.log(message,true);
}

public void doubleClick(WebElement element){
    Actions action= new Actions(driver);
    action.doubleClick(element).build().perform();
}

    public void doubleClickWithJS(WebElement element){
        JavascriptExecutor js= (JavascriptExecutor) driver;
        js.executeScript("arguments[0].dispatchEvent(new MouseEvent('dblclick',{ bubbles: true}));",element);
    }

    protected void verifyPageTitleExact(String expectedPageTitle){
        if(((expectedPageTitle=="")||(expectedPageTitle==null)||(expectedPageTitle.isEmpty()))
                &&(getProperty("browser").equalsIgnoreCase("chrome"))){
            expectedPageTitle=getCurrentURL();
        }
        try{
            wait.waitForPageTitleToBeExact(expectedPageTitle);
            logMessage("TEST PASSED: PageTitle for '"+pageName+"' is exactly: '"+expectedPageTitle+"'");
        }catch(TimeoutException e){
            Assert.fail("Test Failed: PageTitle For"+pageName+ "' is not exactly: '"+expectedPageTitle+"'!!!\n instead it is :- "+driver.getTitle());
        }
    }

    protected void verifyPageTitleContains(){
        String expectedPageTitle = getPageTitleFromFile(pageName).trim();
        verifyPageTitleContains(expectedPageTitle);
    }
protected void verifyPageTitleContains(String expectedPageTitle){
        if (((expectedPageTitle=="")||(expectedPageTitle==null)||(expectedPageTitle.isEmpty()))
                &&(getProperty("browser").equalsIgnoreCase("chrome"))){
            expectedPageTitle=getCurrentURL();
        }
        try{
            wait.waitForPageTitleContains(expectedPageTitle);
            String actualPageTitle=getPageTitle().trim();
            logMessage("ASSERTION PASSED: PageTitle for '"+actualPageTitle+"' contains: '"+expectedPageTitle+"'.");
        }catch(TimeoutException exp){
            String actualPageTitle=getPageTitle().trim();
            logMessage("ASSERTION FAILED: PageTitle for '"+actualPageTitle+"' does not contains: '"+expectedPageTitle+"'.");

        }
}

protected  WebElement getElementByIndex(List<WebElement> elementList, int index){
        return elementList.get(index);
}

protected WebElement getElementByExactText(List<WebElement> elementList,String elementtext){
        WebElement element= null;
        for(WebElement elem : elementList){
            if(elem.getText().equalsIgnoreCase(elementtext.trim())){
                element=elem;
            }
        }
        if (element==null){
            Reporter.log("Element could not found");
        }
        return element;
}

    protected WebElement getElementByContainsText(List<WebElement> elementList,String elementtext){
        WebElement element= null;
        for(WebElement elem : elementList){
            if(elem.getText().contains(elementtext.trim())){
                element=elem;
            }
        }
        // FIXME: handle if no element with text is found in list
        if (element==null){
            Reporter.log("Element could not found");
        }
        return element;
    }

protected void switchToFrames(WebElement element){
        wait.waitForElementToBeVisible(element);
        driver.switchTo().frame(element);
        hardWaitForDemo();
}
protected  void switchToFrame(int i){
        driver.switchTo().frame(i);
}

    protected  void switchToFrame(String id){
        driver.switchTo().frame(id);
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

            public void switchToDefaultContent(){

            driver.switchTo().defaultContent();
            }

            public void navigateBackToPage(){
            driver.navigate().back();
            }

            protected  Object executeJavascript(String script){
        return ((JavascriptExecutor)driver).executeScript(script);}

            protected  String executeJavascript1(String script){
         return(String)((JavascriptExecutor)driver).executeScript(script);}

protected void handleAlert(WebDriver driver){
        try{
            Alert alert= switchToAlert(driver);
            hardWaitForDemo();
            alert.accept();
            logMessage("Alert Handled...");
            driver.switchTo().defaultContent();
        }catch(Exception e){
            System.out.println("No alert window appeared....."+e.getMessage());
        }
}

    private Alert switchToAlert(WebDriver driver) {
        WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(1));
        return wait.until(ExpectedConditions.alertIsPresent());
    }

    public void getJSDropdown(String dropDown,String elementID) throws Exception{
        JavascriptExecutor executor= (JavascriptExecutor) driver;
        String dropdownScript="var select = window.document.getElementById('"+dropDown+
                "'); for(var i=0; i< select.options.length; i++){if(select.options[i].text == '"+elementID+
                "'){select.options[i].selected=true;}}";
        Thread.sleep(2000);
        executor.executeScript(dropdownScript);
        Thread.sleep(2000);
        String clickScript="if ("+"\"createEvent\""+"in document) {var evt= document.createEvent("+"\"HTMLEvents\""+"); " +
                " evt. initEvent("+"\"change\""+ ", false, true); "+dropDown+".dispatchEvent(evt);} else "+
                dropDown+ ".fireEvent("+"\"onchange\""+");";
        executor.executeScript(clickScript);
    }

    protected void selectProvidedTextFromDropDown(WebElement el, String text){
        wait.waitForElementToBeVisible(el);
        scrollDown(el);
        Select sel= new Select(el);
        try{
            sel.selectByVisibleText(text);
            logMessage("Step : Selected option is "+text);
        }catch(StaleElementReferenceException ex1){
            sel.selectByVisibleText(text);
            logMessage("select Element "+el+" after catching stale element exception");
        }catch(Exception ex2){
            sel.selectByVisibleText(text);
        }
    }

    protected void selectProvidedValueFromDropDown(WebElement el, String value){
        wait.waitForElementToBeVisible(el);
        scrollDown(el);
        Select sel= new Select(el);
        try{
            sel.selectByValue(value);
            logMessage("Step : Selected option is "+value);
        }catch(StaleElementReferenceException ex1){
            sel.selectByValue(value);
            logMessage("select Element "+el+" after catching stale element exception");
        }catch(Exception ex2){
            sel.selectByVisibleText(value);
        }
    }

    protected void selectProvidedIndexFromDropDown(WebElement el, int index){
        wait.waitForElementToBeVisible(el);
        scrollDown(el);
        Select sel= new Select(el);
        try{
            sel.selectByIndex(index);
            logMessage("Step : Selected option is "+index);
        }catch(StaleElementReferenceException ex1){
            sel.selectByIndex(index);
            logMessage("select Element "+el+" after catching stale element exception");
        }catch(Exception ex2){
            sel.selectByIndex(index);
        }
    }

    protected void scrollDown(WebElement element) {
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true)",element);
        hardWaitForDemo();
    }
    protected void scrollDown() {
        ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,10000");
        hardWaitForDemo();
    }

    protected void scrollUp() {
        ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,-10000");
        hardWaitForDemo();
    }

    protected void hover(WebElement element){
        Actions hoverOver=new Actions(driver);
        hoverOver.moveToElement(element).build().perform();
    }
    protected String[] hoverAndGetHTML(WebElement element){
        String[] a= new String[2];
        Actions hoverOver=new Actions(driver);
        hoverOver.moveToElement(element).build().perform();
        a[1]=element.getAttribute("outerHTML");
        a[0]=element.getText();
        return a;
    }
    protected void hoverAndClick(WebElement element){
        Actions hoverOver=new Actions(driver);
        hoverOver.moveToElement(element).click().build().perform();
    }
    protected void hoverOnMainAndClickSubLink(WebElement mainElement, WebElement subElement){
        Actions actions=new Actions(driver);
     Actions builder=   actions.moveToElement(mainElement);
        Action b= builder.build();
        b.perform();
        actions.moveToElement(mainElement).build().perform();
        wait.hardWait(1);
        actions.moveToElement(subElement);
        actions.click().build().perform();
    }

    public void mouseHoverJScript(WebElement hoverElement){
        String mouseOverScript="if(document.createEvent){var evObj= document.createEvent('MouseEvents');"
                +"evObj.initEvent('mouseover',true,false); arguments[0].dispatchEvent(evObj);} "
                +"else if(document.createEventObject) {arguments[0].fireEvent(onmouseover');}";
        ((JavascriptExecutor)driver).executeScript(mouseOverScript,hoverElement);

    }

    public void changeWindow(int i){
        String browser= TestSessionInitiator.configSettings.get("browser");
        //Adding block of code to resolve window switching issues in IE-11
        if(browser.equalsIgnoreCase("ie")&&(i>1)){
            wait.waitUntilNewWindowIsOpen(i+1);
            System.out.println("Inside code block");
        String currentWindow= driver.getWindowHandle();
        Set<String> handles= driver.getWindowHandles();
        for(String handle: handles) {

        }
        }
            Set<String> windows= driver.getWindowHandles();
            System.out.println("Window size :: "+windows.size());
            String wins[]= windows.toArray(new String[windows.size()]);
            driver.switchTo().window(wins[i]);

        if(browser.equalsIgnoreCase("ie"))
            wait.hardWait(2);
        else
            hardWaitForDemo();

    }

    public void closeWindow(int i){
        Set<String> windows= driver.getWindowHandles();
        System.out.println("Window size :: "+windows.size());
        String wins[]= windows.toArray(new String[windows.size()]);
        driver.switchTo().window(wins[i]);
        hardWaitForDemo();
        driver.close();
    }
    public void sendText(WebElement e,String text){
        scrollDown(e);
        e.clear();
        e.sendKeys(text);
        hardWaitForDemo();
    }

    public void sendFilePath(WebElement e, String filePath){
        e.sendKeys(filePath);
    }

    public String getAlertText(){
        try{
            Alert alert=driver.switchTo().alert();
            String message=alert.getText();
            alert.accept();
            return message;
        }catch(NoAlertPresentException e){
            return"no alert is present";
        }
    }
    public void acceptAlertPopUp(){
        try{
            Alert alert=driver.switchTo().alert();
            alert.accept();
        }catch (NoAlertPresentException e){
            System.out.println(e.getMessage());
        }
    }

    public boolean isAlertPresent(){
        try{
            driver.switchTo().alert();
            return true;
        }
        catch (NoAlertPresentException e){
            return false;
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
