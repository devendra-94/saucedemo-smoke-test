package com.saucedemo.selcucumber.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;



@CucumberOptions(features="src/test/resources/features/Login.feature",glue="com.saucedemo.selcucumber.stepdefs",plugin={"html:target/cucumber-report","json:target/cucumber.json"})

public class AppLoginPageRunner extends AbstractTestNGCucumberTests{

    }
