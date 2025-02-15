package com.saucedemo.selcucumber.keywords;

import com.saucedemo.automation.getpageobjects.GetPage;
import org.openqa.selenium.WebDriver;

public class AppHomePageActions extends GetPage {
WebDriver driver;
    public AppHomePageActions(WebDriver driver){
        super(driver, "AppLoginPage");
        this.driver=driver;
    }

}
