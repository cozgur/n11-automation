package com.company.qa.tests.steps;

import cucumber.api.java.en.When;

import static com.company.qa.core.util.LogManager.LOGGER;

public class CommonStepDefinitions {

    @When("^I wait for (\\d+) seconds$")
    public void waitForNSeconds(long seconds) throws Exception {
        Thread.sleep(seconds * 1000L);
        LOGGER.info(String.format("\n\tWait for %d seconds\n\t", seconds));
    }
}
