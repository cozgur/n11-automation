package stepDefinitions;

import static resources.logger.LoggerManagement.LOGGER;

import cucumber.api.java.en.When;

public class commonStepDefinitions {

    @When("^I wait for (\\d+) seconds$")
    public void waitForNSeconds(long seconds) throws Exception {
        Thread.sleep(seconds * 1000L);
        LOGGER.info(String.format("\n\tWait for %d seconds\n\t", seconds));

    }

}
