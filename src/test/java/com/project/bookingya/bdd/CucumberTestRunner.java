package com.project.bookingya.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.project.bookingya.bdd",
        plugin = {"pretty", "html:target/cucumber-reports.html",
                "json:target/cucumber.json"},
        objectFactory = cucumber.runtime.SerenityObjectFactory.class,
        monochrome = true
)
public class CucumberTestRunner {
}

