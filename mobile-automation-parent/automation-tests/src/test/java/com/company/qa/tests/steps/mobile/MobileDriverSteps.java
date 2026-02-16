package com.company.qa.tests.steps.mobile;

import com.google.gson.JsonObject;
import com.company.qa.core.driver.MobileDriverManager;
import com.company.qa.core.util.JsonParser;
import com.company.qa.tests.steps.BaseStepDefinition;
import com.company.qa.tests.steps.ScenarioState;
import io.appium.java_client.AppiumDriver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.company.qa.core.util.LogManager.LOGGER;

/**
 * Step definitions for mobile driver setup and page navigation.
 */
public class MobileDriverSteps extends BaseStepDefinition {

    public MobileDriverSteps(ScenarioState state) {
        super(state);
    }

    private AppiumDriver getDriver() {
        return MobileDriverManager.getDriver();
    }

    @Given("^I use mobile driver from config$")
    public void useMobileDriverFromConfig() {
        MobileDriverManager.createDriverFromConfig();
    }

    @Given("^I use (android|ios) mobile driver on device \"([^\"]*)\" version \"([^\"]*)\"$")
    public void useMobileDriver(String platform, String deviceName, String platformVersion) {
        MobileDriverManager.createDriver(platform, deviceName, platformVersion);
        LOGGER.info(String.format("\n\t[Thread-%d] Mobile driver created: [%s] device [%s] version [%s]\n\t",
                Thread.currentThread().getId(), platform, deviceName, platformVersion));
    }

    @Given("^I use (android|ios) mobile driver on device \"([^\"]*)\" version \"([^\"]*)\" with app \"([^\"]*)\"$")
    public void useMobileDriverWithApp(String platform, String deviceName, String platformVersion, String app) {
        MobileDriverManager.createDriverWithApp(platform, deviceName, platformVersion, app);
        LOGGER.info(String.format("\n\t[Thread-%d] Mobile driver created: [%s] device [%s] app [%s]\n\t",
                Thread.currentThread().getId(), platform, deviceName, app));
    }

    @Given("^I use (android|ios) mobile driver on device \"([^\"]*)\" version \"([^\"]*)\" at \"([^\"]*)\"$")
    public void useMobileDriverAtUrl(String platform, String deviceName, String platformVersion, String appiumUrl) {
        MobileDriverManager.createDriver(platform, deviceName, platformVersion, appiumUrl);
        LOGGER.info(String.format("\n\t[Thread-%d] Mobile driver created: [%s] at [%s]\n\t",
                Thread.currentThread().getId(), platform, appiumUrl));
    }

    @Given("^I use (android|ios) mobile driver with capabilities$")
    public void useMobileDriverWithCapabilities(String platform, DataTable table) {
        Map<String, String> capabilities = new HashMap<>();
        for (List<String> row : table.asLists(String.class)) {
            capabilities.put(row.get(0), row.get(1));
        }

        String appiumUrl = capabilities.remove("appiumUrl");
        if (appiumUrl != null) {
            MobileDriverManager.createDriverWithCapabilities(platform, capabilities, appiumUrl);
        } else {
            MobileDriverManager.createDriverWithCapabilities(platform, capabilities);
        }

        LOGGER.info(String.format("\n\t[Thread-%d] Mobile driver created: [%s] with capabilities %s\n\t",
                Thread.currentThread().getId(), platform, capabilities));
    }

    @When("^I open mobile (\\w+(?: \\w+)*) page$")
    public void iOpenMobilePage(String flowKey) {
        JsonObject jsonObject = JsonParser.parse("pages");
        state.setPageObject(jsonObject.get(flowKey).getAsJsonObject());
        String urlString = state.getPageObject().get("url").getAsString();
        getDriver().get(urlString);
        LOGGER.info(String.format("\n\tMobile navigated to: %s\n\t", urlString));
    }
}
