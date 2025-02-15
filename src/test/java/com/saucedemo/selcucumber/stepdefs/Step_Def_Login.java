package com.saucedemo.selcucumber.stepdefs;

import com.saucedemo.automation.TestSessionInitiator;
import static com.saucedemo.selcucumber.stepdefs.Step_Def_Base.test;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class Step_Def_Login {

    @Given("User enter the url and launch application")
    public void launch_application(){
        test= new TestSessionInitiator(super.getClass().getSimpleName());
        test.launchApplication();
    }

    @When("User enter the username and password")
    public void userEnterTheUsernameAndPassord(){
        test.loginPage.login(System.getProperty("sauceUser"),System.getProperty("saucePassword"));
    }
}
