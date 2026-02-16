package com.company.qa.tests.steps.mobile;

import com.google.gson.JsonObject;
import com.company.qa.core.driver.MobileDriverManager;
import com.company.qa.core.driver.SelectDecision;
import com.company.qa.core.action.MobileActions;
import com.company.qa.tests.steps.BaseStepDefinition;
import com.company.qa.tests.steps.ScenarioState;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.company.qa.core.util.LogManager.LOGGER;

/**
 * Step definitions for mobile element interactions
 * (type, clear, fill, tap by accessibility id).
 */
public class MobileElementSteps extends BaseStepDefinition {

    public MobileElementSteps(ScenarioState state) {
        super(state);
    }

    private AppiumDriver getDriver() {
        return MobileDriverManager.getDriver();
    }

    @Then("^I type \"([^\"]*)\" on mobile element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iTypeOnMobileElement(String text, String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        getDriver().findElement(by).sendKeys(text);
        LOGGER.info(String.format("\n\tTyped [%s] on mobile element: [%s]\n\t", text, pageKey));
    }

    @Then("^I clear mobile element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iClearMobileElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        getDriver().findElement(by).clear();
        LOGGER.info(String.format("\n\tCleared mobile element: [%s]\n\t", pageKey));
    }

    @Then("^I fill mobile by (\\w+(?: \\w+)*)$")
    public void iFillMobileBy(String selectKey, DataTable table) {
        for (List<String> row : table.asLists(String.class)) {
            String key = row.get(0);
            String value = row.get(1);
            JsonObject pageElementObject = state.getPageObject().get("elements").getAsJsonObject();
            String pageElement = pageElementObject.get(key).getAsString();
            By by = SelectDecision.resolve(selectKey, pageElement);
            state.setCurrentLocator(by);
            getDriver().findElement(by).clear();
            getDriver().findElement(by).sendKeys(value);
            LOGGER.info(String.format("\n\tMobile filling key: [%s] with value: [%s]\n\t", key, value));
        }
    }

    @Then("^I tap mobile by (\\w+(?: \\w+)*)$")
    public void iTapMobileBy(String selectKey, DataTable table) {
        for (List<String> row : table.asLists(String.class)) {
            String key = row.get(0);
            JsonObject pageElementObject = state.getPageObject().get("elements").getAsJsonObject();
            String pageElement = pageElementObject.get(key).getAsString();
            By by = SelectDecision.resolve(selectKey, pageElement);
            state.setCurrentLocator(by);
            WebElement element = getDriver().findElement(by);
            new MobileActions(getDriver()).tap(element);
            LOGGER.info(String.format("\n\tMobile tapped element: [%s]\n\t", key));
        }
    }

    @Then("^I tap element with accessibility id \"([^\"]*)\"$")
    public void iTapByAccessibilityId(String accessibilityId) {
        WebElement element = getDriver().findElement(AppiumBy.accessibilityId(accessibilityId));
        new MobileActions(getDriver()).tap(element);
        LOGGER.info(String.format("\n\tTapped element with accessibility ID: [%s]\n\t", accessibilityId));
    }

    @Then("^I type \"([^\"]*)\" on element with accessibility id \"([^\"]*)\"$")
    public void iTypeByAccessibilityId(String text, String accessibilityId) {
        getDriver().findElement(AppiumBy.accessibilityId(accessibilityId)).sendKeys(text);
        LOGGER.info(String.format("\n\tTyped [%s] on element with accessibility ID: [%s]\n\t", text, accessibilityId));
    }
}
