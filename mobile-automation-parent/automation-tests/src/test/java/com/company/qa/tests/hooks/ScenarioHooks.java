package com.company.qa.tests.hooks;

import com.company.qa.core.context.TestContext;
import com.company.qa.core.driver.BrowserManager;
import com.company.qa.core.driver.MobileDriverManager;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;

import static com.company.qa.core.util.LogManager.LOGGER;

public class ScenarioHooks {

    private static int scenarioCounter = 0;
    private static int failedScenarioCounter = 0;

    @Before
    public void beforeScenario(Scenario scenario) {
        ++scenarioCounter;
        LOGGER.info(String.format("\n\n\t[%d] > Scenario [%s] started\t", scenarioCounter, scenario.getName()));
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            ++failedScenarioCounter;
        }

        String result = scenario.isFailed() ? "with errors" : "successfully";
        LOGGER.info(String.format("\n\t[%d] > Scenario [%s] finished %s\t", scenarioCounter, scenario.getName(), result));
        LOGGER.info(String.format("\n\t%d of %d scenarios failed so far\t", failedScenarioCounter, scenarioCounter));

        // Cleanup browser driver if it was used
        if (BrowserManager.getDriver() != null) {
            BrowserManager.removeDriver();
        }

        // Cleanup test context
        TestContext.reset();
    }

    @After("@mobile")
    public void afterMobileScenario(Scenario scenario) {
        MobileDriverManager.removeDriver();
    }
}
