package com.company.qa.tests.steps.web;

import com.google.gson.JsonObject;
import com.company.qa.core.driver.SelectDecision;
import com.company.qa.tests.steps.BaseStepDefinition;
import com.company.qa.tests.steps.ScenarioState;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static com.company.qa.core.util.LogManager.LOGGER;

/**
 * Step definitions for web element interactions
 * (click, double click, right click, fill, clear, scroll, submit, hover, dropdown).
 */
public class WebElementSteps extends BaseStepDefinition {

    public WebElementSteps(ScenarioState state) {
        super(state);
    }

    private WebDriver getWebDriver() {
        return state.getWebDriver();
    }

    @Then("^I click by (\\w+(?: \\w+)*)$")
    public void iClickBy(String selectKey, DataTable table) {
        for (List<String> row : table.asLists(String.class)) {
            String key = row.get(0);
            By by = resolveElement(key, selectKey);
            getWebDriver().findElement(by).click();
            LOGGER.info(String.format("\n\tClicked element: [%s]\n\t", key));
        }
    }

    @Then("^I click element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iClickElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        getWebDriver().findElement(by).click();
        LOGGER.info(String.format("\n\tClicked element: [%s]\n\t", pageKey));
    }

    @Then("^I double click element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iDoubleClickElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        WebElement element = getWebDriver().findElement(by);
        new Actions(getWebDriver()).doubleClick(element).perform();
        LOGGER.info(String.format("\n\tDouble clicked element: [%s]\n\t", pageKey));
    }

    @Then("^I right click element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iRightClickElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        WebElement element = getWebDriver().findElement(by);
        new Actions(getWebDriver()).contextClick(element).perform();
        LOGGER.info(String.format("\n\tRight clicked element: [%s]\n\t", pageKey));
    }

    @When("^I click (\\w+(?: \\w+)*), link: (\\w+(?: \\w+)*) opened in new tab$")
    public void iClickedNewTab(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        WebElement element = getWebDriver().findElement(by);
        Actions action = new Actions(getWebDriver());
        action.keyDown(Keys.COMMAND).keyDown(Keys.SHIFT).click(element).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();
        LOGGER.info(String.format("\n\tClicking link and opened in new tab %s\n\t", pageKey));
    }

    @Then("^I fill by (\\w+(?: \\w+)*)$")
    public void iFillBy(String selectKey, DataTable table) {
        for (List<String> row : table.asLists(String.class)) {
            String key = row.get(0);
            String value = row.get(1);
            JsonObject pageElementObject = state.getPageObject().get("elements").getAsJsonObject();
            String pageElement = pageElementObject.get(key).getAsString();
            By by = SelectDecision.resolve(selectKey, pageElement);
            state.setCurrentLocator(by);
            getWebDriver().findElements(by).clear();
            getWebDriver().findElement(by).sendKeys(value);
            LOGGER.info(String.format("\n\tFilling the key: [%s] \t with the value: [%s]\n\t", key, value));
        }
    }

    @Then("^I clear element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iClearElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        getWebDriver().findElement(by).clear();
        LOGGER.info(String.format("\n\tCleared element: [%s]\n\t", pageKey));
    }

    @Then("^I scroll to element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iScrollToElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        WebElement element = getWebDriver().findElement(by);
        ((JavascriptExecutor) getWebDriver()).executeScript("arguments[0].scrollIntoView(true);", element);
        LOGGER.info(String.format("\n\tScrolled to element: [%s]\n\t", pageKey));
    }

    @Then("^I submit element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iSubmitElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        getWebDriver().findElement(by).submit();
        LOGGER.info(String.format("\n\tSubmitted form via element: [%s]\n\t", pageKey));
    }

    @Then("^I mouse hover to element (\\w+(?: \\w+)*)$")
    public void iMouseHover(WebElement element) {
        Actions action = new Actions(getWebDriver());
        action.moveToElement(element).perform();
        LOGGER.info(String.format("\n\tMouse hover to element: %s\n\t", element));
    }

    @Then("I mouse hover to element (\\w+(?: \\w+)*) and click")
    public void iMouseClick(WebElement element) {
        Actions action = new Actions(getWebDriver());
        action.moveToElement(element).click(element).build().perform();
        LOGGER.info(String.format("\n\tMouse hover and click to element: %s\n\t", element));
    }

    @Then("^I select (\\w+(?: \\w+)*) dropdown option on element (\\w+(?: \\w+)*)$")
    public void iSelectDropdownOption(String option, String element) {
        WebElement dropdown = getWebDriver().findElement(By.tagName("option"));
        Select dd = new Select(dropdown);
        dd.selectByVisibleText(option);
        dd.selectByValue(element);
    }
}
