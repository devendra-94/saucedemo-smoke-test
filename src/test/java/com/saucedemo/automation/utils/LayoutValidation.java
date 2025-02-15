package com.saucedemo.automation.utils;

import freemarker.template.TemplateException;
import net.mindengine.galen.api.Galen;
import net.mindengine.galen.reports.GalenTestInfo;
import net.mindengine.galen.reports.HtmlReportBuilder;
import net.mindengine.galen.reports.model.LayoutReport;
import net.mindengine.galen.validation.ValidationError;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LayoutValidation {

    private WebDriver driver;
    private String PageName;
    private String tier;
    private String filePath = "src/test/resources/PageObjectRepository/";

    // Constructor to initialize the driver and pageName
    public LayoutValidation(WebDriver driver, String pageName) {
        this.driver = driver;
        this.PageName = pageName;
    }

    // Method to check the layout of the page based on the given tags
    public void checkLayout(List<String> tagsToBeTested) {

        try {
            // Run the layout check using Galen API
            LayoutReport layoutReport = Galen.checkLayout(this.driver, this.filePath + this.tier + this.PageName + ".spec", tagsToBeTested, null, null, null);

            // Create a list of Galen test info to build the report
            LinkedList<GalenTestInfo> tests = new LinkedList<>();
            GalenTestInfo test = GalenTestInfo.fromString(this.PageName + " - layout test");
            test.getReport().layout(layoutReport, this.PageName + " - layout test");
            tests.add(test);

            // Generate the HTML report
            new HtmlReportBuilder().build(tests, "target/galen-reports");

            // If there are errors in the layout report, print the error messages
            if (layoutReport.errors() > 0) {
                for (ValidationError error : layoutReport.getValidationErrors()) {
                    for (String errorMsg : error.getMessages()) {
                        System.out.println(errorMsg);
                    }
                }
            }

        } catch (IOException ex) {
            // Handle IOException
            System.err.println("IOException occurred: " + ex.getMessage());
            ex.printStackTrace();
        } catch (TemplateException ex) {
            // Handle TemplateException
            System.err.println("TemplateException occurred: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            // Handle any other generic exceptions
            System.err.println("An unexpected error occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
