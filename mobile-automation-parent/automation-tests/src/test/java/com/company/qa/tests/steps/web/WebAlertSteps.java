package com.company.qa.tests.steps.web;

import com.company.qa.tests.steps.BaseStepDefinition;
import com.company.qa.tests.steps.ScenarioState;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

import static com.company.qa.core.util.LogManager.LOGGER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for web alert handling and keyboard actions
 * (accept, dismiss, alert text, type into alert, enter, escape, tab).
 */
public class WebAlertSteps extends BaseStepDefinition {

    public WebAlertSteps(ScenarioState state) {
        super(state);
    }

    private WebDriver getWebDriver() {
        return state.getWebDriver();
    }

    @Then("^I accept the alert$")
    public void iAcceptAlert() {
        getWebDriver().switchTo().alert().accept();
        LOGGER.info("\n\tAlert accepted\n\t");
    }

    @Then("^I dismiss the alert$")
    public void iDismissAlert() {
        getWebDriver().switchTo().alert().dismiss();
        LOGGER.info("\n\tAlert dismissed\n\t");
    }

    @Then("^I see alert text equals \"([^\"]*)\"$")
    public void iSeeAlertTextEquals(String expectedText) {
        String alertText = getWebDriver().switchTo().alert().getText();
        assertThat(alertText)
                .as("Expected alert text [%s] but got [%s]", expectedText, alertText)
                .isEqualTo(expectedText);
        LOGGER.info(String.format("\n\tAlert text is [%s] as expected\n\t", alertText));
    }

    @Then("^I type \"([^\"]*)\" into alert$")
    public void iTypeIntoAlert(String text) {
        getWebDriver().switchTo().alert().sendKeys(text);
        LOGGER.info(String.format("\n\tTyped [%s] into alert\n\t", text));
    }

    @Then("^I press enter on element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iPressEnterOnElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        getWebDriver().findElement(by).sendKeys(Keys.ENTER);
        LOGGER.info(String.format("\n\tPressed ENTER on element: [%s]\n\t", pageKey));
    }

    @Then("^I press escape$")
    public void iPressEscape() {
        new Actions(getWebDriver()).sendKeys(Keys.ESCAPE).perform();
        LOGGER.info("\n\tPressed ESCAPE\n\t");
    }

    @Then("^I press tab on element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iPressTabOnElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        getWebDriver().findElement(by).sendKeys(Keys.TAB);
        LOGGER.info(String.format("\n\tPressed TAB on element: [%s]\n\t", pageKey));
    }
}
