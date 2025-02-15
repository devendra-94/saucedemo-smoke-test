package com.saucedemo.selcucumber.stepdefs;

import com.saucedemo.automation.TestSessionInitiator;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Step_Def_Base {
    public static TestSessionInitiator test;

    @Before
    public void initialSetUp(){

    }
    @After
    public void screenShotAndConsoleLog(Scenario result){
        screenShot(result);
        System.out.println(result.getStatus().toString());
        try{
            if(!result.getStatus().toString().equalsIgnoreCase("passed")){
                throw new RuntimeException((result.getName()+"failed"));
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    private void screenShot(Scenario result){
        if (test != null && test.takescreenshot!=null) {
 test.takescreenshot.takeScreenshotOnException((result));
        }
        else{
            System.out.println("screenshot service is not initialized");
        }
    }

}
