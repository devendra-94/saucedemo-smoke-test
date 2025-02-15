package com.saucedemo.automation;

import com.saucedemo.automation.utils.TakeScreenshot;
import com.saucedemo.selcucumber.keywords.AppHomePageActions;
import com.saucedemo.selcucumber.keywords.AppLoginPageActions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.saucedemo.automation.utils.ConfigPropertyReader;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.saucedemo.automation.utils.YamlReader.*;

public class TestSessionInitiator {


    protected WebDriver driver;
    private final WebDriverFactory wdfactory;

    String browser;
    String seleniumserver;
    String appbaseurl;
    String applicationpath;
    String chromedriverpath;
    String datafileloc="";
    Map<String,Object> chromeOptions=null;
    DesiredCapabilities capabilities;

public static Map<String, String> configSettings= new HashMap<String,String>();
public AppLoginPageActions loginPage;
public AppHomePageActions homePage;
public TakeScreenshot takescreenshot;

public WebDriver getDriver(){
    return this.driver;
}

private void _initPage(){
    loginPage=new AppLoginPageActions(driver);
    homePage =new AppHomePageActions(driver);
}

public TestSessionInitiator(String testName){
    _getSessionConfig();
    wdfactory=new WebDriverFactory();
    testInitiator(testName);
}

private void testInitiator(String testName){
    setYamlFilePath();
    _configureBrowser();
    _initPage();
    takescreenshot= new TakeScreenshot(testName,this.driver);
}

private void _configureBrowser(){
    driver= wdfactory.getDriver(configSettings);
    driver.manage().window().maximize();
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
}
//    public static WebDriver getChromeDriver(String driverpath){
//        System.setProperty("webdriver.chrome.driver", driverpath);
//        ChromeOptions options = new ChromeOptions();
////        options.addArguments("--headless");
//        options.addArguments("--disable-gpu");
//        return new ChromeDriver(options);
//    }
public static Map<String, String> _getSessionConfig(){

    configSettings = ConfigPropertyReader.readAllPropertyValuesFromConfigFile();
    Properties prop=System.getProperties();
for(Object ob:configSettings.keySet()){
    if(prop.keySet().contains(ob)){
        configSettings.replace(ob.toString(),prop.get(ob).toString());
    }
}
    System.out.println(configSettings);
    return configSettings;

    }

   public void launchApplication(){
    String url= getYamlValue("app_url");
    launchApplication(url);
   }
public void launchApplication(String baseurl){
    System.out.println(".........................."+configSettings.get("browser")+"......................");

    driver.manage().deleteAllCookies();
    driver.get(baseurl);

}


public void closeBrowserSession(){
    driver.quit();
}

}
