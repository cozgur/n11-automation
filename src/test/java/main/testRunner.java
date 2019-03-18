package main;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/",
        glue = {"stepDefinitions"},
        tags = {"~@smoke"}
)
public class testRunner {
}