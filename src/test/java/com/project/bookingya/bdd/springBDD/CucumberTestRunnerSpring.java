package com.project.bookingya.bdd.springBDD;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.project.bookingya.bdd.springBDD",
        plugin = {"pretty", "html:target/cucumber-spring-reports.html"},
        tags = "@reservas",
        objectFactory = io.cucumber.spring.SpringFactory.class
)
public class CucumberTestRunnerSpring {
}
