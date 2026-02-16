package com.company.qa.tests.steps.web;

import com.google.gson.JsonObject;
import com.company.qa.core.driver.SelectDecision;
import com.company.qa.tests.steps.BaseStepDefinition;
import com.company.qa.tests.steps.ScenarioState;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.company.qa.core.util.LogManager.LOGGER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for web assertions
 * (title, text, URL, element visibility, enabled/disabled, attributes, count).
 */
public class WebAssertionSteps extends BaseStepDefinition {

    public WebAssertionSteps(ScenarioState state) {
        super(state);
    }

    private WebDriver getWebDriver() {
        return state.getWebDriver();
    }

    @Then("^I see webpage title equals \"([^\"]*)\"$")
    public void webpageTitleEquals(String expectedHeader) {
        String currentHeader = getWebDriver().getTitle();
        assertThat(currentHeader)
                .as("Expected webpage title [%s] but got [%s]", expectedHeader, currentHeader)
                .isEqualTo(expectedHeader);
        LOGGER.info(String.format("\n\tThe webpage title is [%s] as expected [%s]\n\t", currentHeader, expectedHeader));
    }

    @Then("^I see webpage title contains \"([^\"]*)\"$")
    public void webpageTitleContains(String expectedHeader) {
        String currentHeader = getWebDriver().getTitle();
        assertThat(currentHeader)
                .as("Expected webpage title [%s] to contain [%s]", currentHeader, expectedHeader)
                .contains(expectedHeader);
        LOGGER.info(String.format("\n\tThe webpage title [%s] contains [%s]\n\t", currentHeader, expectedHeader));
    }

    @Then("^I see webpage title does not equal \"([^\"]*)\"$")
    public void webpageTitleNotEqual(String expectedHeader) {
        String currentHeader = getWebDriver().getTitle();
        assertThat(currentHeader)
                .as("Expected webpage title to NOT equal [%s] but it does", expectedHeader)
                .isNotEqualTo(expectedHeader);
        LOGGER.info(String.format("\n\tThe webpage title [%s] does not equal [%s] as expected\n\t", currentHeader, expectedHeader));
    }

    @Then("^I see webpage title does not contain \"([^\"]*)\"$")
    public void webpageTitleNotContain(String expectedHeader) {
        String currentHeader = getWebDriver().getTitle();
        assertThat(currentHeader)
                .as("Expected webpage title [%s] to NOT contain [%s] but it does", currentHeader, expectedHeader)
                .doesNotContain(expectedHeader);
        LOGGER.info(String.format("\n\tThe webpage title [%s] does not contain [%s] as expected\n\t", currentHeader, expectedHeader));
    }

    @And("^I see text$")
    public void iSeeText(DataTable table) {
        JsonObject pageElementObject = state.getPageObject().get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get("mainPanel").getAsString();
        state.setCurrentLocator(SelectDecision.resolve("xpath", pageElement));

        for (List<String> row : table.asLists(String.class)) {
            String key = row.get(0);
            boolean isThere = getWebDriver().getPageSource().contains(key);
            if (isThere) {
                LOGGER.info(String.format("\n\tThe text is in the page: %s\n\t", key));
            } else {
                throw new AssertionError(String.format("\n\tThe text is NOT in the page: %s\n\t", key));
            }
        }
    }

    @Then("^I see (\\w+(?: \\w+)*) equals to \"([^\"]*)\"$")
    public void iSeeElement(String pageKey, String valueKey) {
        JsonObject pageElementObject = state.getPageObject().get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        String element = getWebDriver().findElement(By.xpath(pageElement)).getText();
        assertThat(element).isEqualTo(valueKey);
        LOGGER.info(String.format("\n\tCheck web element: %s\n\t", pageKey));
    }

    @Then("^I see current url equals \"([^\"]*)\"$")
    public void iSeeCurrentUrlEquals(String expectedUrl) {
        String currentUrl = getWebDriver().getCurrentUrl();
        assertThat(currentUrl)
                .as("Expected URL [%s] but got [%s]", expectedUrl, currentUrl)
                .isEqualTo(expectedUrl);
        LOGGER.info(String.format("\n\tCurrent URL [%s] equals expected [%s]\n\t", currentUrl, expectedUrl));
    }

    @Then("^I see current url contains \"([^\"]*)\"$")
    public void iSeeCurrentUrlContains(String expectedText) {
        String currentUrl = getWebDriver().getCurrentUrl();
        assertThat(currentUrl)
                .as("Expected URL [%s] to contain [%s]", currentUrl, expectedText)
                .contains(expectedText);
        LOGGER.info(String.format("\n\tCurrent URL [%s] contains [%s]\n\t", currentUrl, expectedText));
    }

    @Then("^I see element (\\w+(?: \\w+)*) is displayed by (\\w+(?: \\w+)*)$")
    public void iSeeElementIsDisplayed(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        boolean isDisplayed = getWebDriver().findElement(by).isDisplayed();
        assertThat(isDisplayed)
                .as("Expected element [%s] to be displayed but it is not", pageKey)
                .isTrue();
        LOGGER.info(String.format("\n\tElement [%s] is displayed\n\t", pageKey));
    }

    @Then("^I see element (\\w+(?: \\w+)*) is not displayed by (\\w+(?: \\w+)*)$")
    public void iSeeElementIsNotDisplayed(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        List<WebElement> elements = getWebDriver().findElements(by);
        boolean isNotDisplayed = elements.isEmpty() || !elements.get(0).isDisplayed();
        assertThat(isNotDisplayed)
                .as("Expected element [%s] to NOT be displayed but it is", pageKey)
                .isTrue();
        LOGGER.info(String.format("\n\tElement [%s] is not displayed\n\t", pageKey));
    }

    @Then("^I see element (\\w+(?: \\w+)*) is enabled by (\\w+(?: \\w+)*)$")
    public void iSeeElementIsEnabled(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        boolean isEnabled = getWebDriver().findElement(by).isEnabled();
        assertThat(isEnabled)
                .as("Expected element [%s] to be enabled but it is not", pageKey)
                .isTrue();
        LOGGER.info(String.format("\n\tElement [%s] is enabled\n\t", pageKey));
    }

    @Then("^I see element (\\w+(?: \\w+)*) is disabled by (\\w+(?: \\w+)*)$")
    public void iSeeElementIsDisabled(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        boolean isEnabled = getWebDriver().findElement(by).isEnabled();
        assertThat(isEnabled)
                .as("Expected element [%s] to be disabled but it is enabled", pageKey)
                .isFalse();
        LOGGER.info(String.format("\n\tElement [%s] is disabled\n\t", pageKey));
    }

    @Then("^I see element (\\w+(?: \\w+)*) attribute \"([^\"]*)\" equals \"([^\"]*)\" by (\\w+(?: \\w+)*)$")
    public void iSeeElementAttributeEquals(String pageKey, String attribute, String expectedValue, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        String actualValue = getWebDriver().findElement(by).getAttribute(attribute);
        assertThat(actualValue)
                .as("Expected attribute [%s] of element [%s] to be [%s] but got [%s]",
                        attribute, pageKey, expectedValue, actualValue)
                .isEqualTo(expectedValue);
        LOGGER.info(String.format("\n\tElement [%s] attribute [%s] equals [%s]\n\t", pageKey, attribute, expectedValue));
    }

    @Then("^I see element count (\\w+(?: \\w+)*) is (\\d+) by (\\w+(?: \\w+)*)$")
    public void iSeeElementCount(String pageKey, int expectedCount, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        int actualCount = getWebDriver().findElements(by).size();
        assertThat(actualCount)
                .as("Expected [%d] elements for [%s] but found [%d]",
                        expectedCount, pageKey, actualCount)
                .isEqualTo(expectedCount);
        LOGGER.info(String.format("\n\tElement [%s] count is [%d] as expected\n\t", pageKey, expectedCount));
    }
}
