package com.saucedemo.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class WebDriverFactory {
  public  static String browser;
WebDriver driver;

    public WebDriver getDriver(Map<String, String> seleniumConfig) {
        System.out.println("getDriver method");
        String browser = seleniumConfig.get("browser");

        if (seleniumConfig.get("seleniumserver").equalsIgnoreCase("local")) {
            switch (browser.toLowerCase()) {
                case "chrome":
                    String driverPathChrome = seleniumConfig.get("driverpathChrome");
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        return getChromeDriver(seleniumConfig.get("driverpathChrome")+".exe");
                    } else if (System.getProperty("os.name").toLowerCase().contains("linux")
                            || System.getProperty("os.name").toLowerCase().contains("mac")) {
                        return getChromeDriver(driverPathChrome);
                    }
                    break;

                case "safari":
                    return new SafariDriver();

                case "ie":
                case "internetexplorer":
                    return getInternetExplorerDriver(seleniumConfig.get("driverpathIE"));

                // Add any other browser options here as needed

                default:
                    System.out.println("Browser type not supported: " + browser);
            }
        }

        if (seleniumConfig.get("seleniumserver").equalsIgnoreCase("remote")) {
            return setRemoteDriver(seleniumConfig);
        }

        return new FirefoxDriver();
    }
private WebDriver setRemoteDriver(Map<String, String> selConfig){
    String browser= selConfig.getOrDefault("browser","chrome").toLowerCase();
    String seleniumHubAddress=selConfig.get("seleniumserverhost");
    URL seleniumServerHost;
    try{
        seleniumServerHost = new URL(seleniumHubAddress);
    } catch (MalformedURLException e) {
        throw new RuntimeException("Invalid selenium address : "+ seleniumHubAddress, e);
    }
    switch (browser){
        case "Firefox":
            FirefoxOptions firefoxOptions= new FirefoxOptions();
            driver = new RemoteWebDriver(seleniumServerHost,firefoxOptions);
            break;
        case "chrome":
            ChromeOptions chromeOptions= new ChromeOptions();
            chromeOptions.addArguments("disable-popup-blocking","true");
            chromeOptions.addArguments("--disable-extensions");
            chromeOptions.addArguments("ignore-certificate-errors");
            driver = new RemoteWebDriver(seleniumServerHost,chromeOptions);
            break;

        case "safari":
            SafariOptions safariOptions= new SafariOptions();
            driver = new RemoteWebDriver(seleniumServerHost,safariOptions);
            break;

        case "ie":
        case"internetexplorer":
        case"internet explorer":
            InternetExplorerOptions internetExplorerOptions=new InternetExplorerOptions();
            driver= new RemoteWebDriver(seleniumServerHost,internetExplorerOptions);
            break;
        default:
            throw new IllegalArgumentException("Unsupported browser: "+ browser);
    }
    return  driver;
}

  public static WebDriver getChromeDriver(String driverpath){
    System.setProperty("webdriver.chrome.driver", driverpath);
      ChromeOptions chromeOptions= new ChromeOptions();
      chromeOptions.addArguments("disable-popup-blocking","true");
      chromeOptions.addArguments("--disable-extensions");
      chromeOptions.addArguments("ignore-certificate-errors");
      return new ChromeDriver(chromeOptions);
    }

    private WebDriver getInternetExplorerDriver(String driverpath){
    System.setProperty("webdriver.ie.driver",driverpath);
    return new InternetExplorerDriver();
    }
}



