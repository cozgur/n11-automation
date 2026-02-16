package com.company.qa.tests.steps.mobile;

import com.company.qa.core.driver.MobileDriverManager;
import com.company.qa.tests.steps.BaseStepDefinition;
import com.company.qa.tests.steps.ScenarioState;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.When;

import java.time.Duration;

import static com.company.qa.core.util.LogManager.LOGGER;

/**
 * Step definitions for mobile app lifecycle operations
 * (launch, close, reset, background).
 */
public class MobileAppLifecycleSteps extends BaseStepDefinition {

    public MobileAppLifecycleSteps(ScenarioState state) {
        super(state);
    }

    private AppiumDriver getDriver() {
        return MobileDriverManager.getDriver();
    }

    @When("^I launch the mobile app$")
    public void iLaunchApp() {
        getDriver().activateApp(MobileDriverManager.getAppId());
        LOGGER.info("\n\tMobile app launched\n\t");
    }

    @When("^I close the mobile app$")
    public void iCloseApp() {
        getDriver().terminateApp(MobileDriverManager.getAppId());
        LOGGER.info("\n\tMobile app closed\n\t");
    }

    @When("^I reset the mobile app$")
    public void iResetApp() {
        String appId = MobileDriverManager.getAppId();
        getDriver().terminateApp(appId);
        getDriver().activateApp(appId);
        LOGGER.info("\n\tMobile app reset\n\t");
    }

    @When("^I send the mobile app to background for (\\d+) seconds$")
    public void iBackgroundApp(int seconds) {
        getDriver().runAppInBackground(Duration.ofSeconds(seconds));
        LOGGER.info(String.format("\n\tMobile app sent to background for [%d] seconds\n\t", seconds));
    }
}
