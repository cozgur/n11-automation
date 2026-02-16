package com.company.qa.tests.steps;

import io.cucumber.java.en.When;

import static com.company.qa.core.util.LogManager.LOGGER;

public class CommonStepDefinitions extends BaseStepDefinition {

    public CommonStepDefinitions(ScenarioState state) {
        super(state);
    }

    @When("^I wait for (\\d+) seconds$")
    public void waitForNSeconds(long seconds) throws Exception {
        Thread.sleep(seconds * 1000L);
        LOGGER.info(String.format("\n\tWait for %d seconds\n\t", seconds));
    }
}
