package com.saucedemo.selcucumber.keywords;

import com.saucedemo.automation.getpageobjects.GetPage;
import org.openqa.selenium.WebDriver;

public class AppLoginPageActions extends GetPage {
WebDriver driver;
public AppLoginPageActions(WebDriver driver){
    super(driver, "AppLoginPage");
    this.driver=driver;
}

public void login(String username, String password){
element("username").sendKeys(username);
element("password").sendKeys(password);
click(element("login"));

}
}
