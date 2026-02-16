package com.company.qa.tests.steps.web;

import com.google.gson.JsonObject;
import com.company.qa.core.driver.BrowserManager;
import com.company.qa.core.util.JsonParser;
import com.company.qa.tests.steps.BaseStepDefinition;
import com.company.qa.tests.steps.ScenarioState;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.ArrayList;

import static com.company.qa.core.util.LogManager.LOGGER;

/**
 * Step definitions for web driver setup, page navigation,
 * tabs, iFrames, and waiting.
 */
public class WebNavigationSteps extends BaseStepDefinition {

    public WebNavigationSteps(ScenarioState state) {
        super(state);
    }

    private WebDriver getWebDriver() {
        return state.getWebDriver();
    }

    @Given("^I use (\\w+(?: \\w+)*) driver")
    public void useDriver(String browserKey) {
        WebDriver webDriver = BrowserManager.createBrowser(browserKey);
        state.setWebDriver(webDriver);
        LOGGER.info(String.format("\n\tDriver is: %s\n\t", browserKey));
    }

    @When("^I open (\\w+(?: \\w+)*) page$")
    public void iOpenPage(String flowKey) {
        JsonObject jsonObject = JsonParser.parse("pages");
        state.setPageObject(jsonObject.get(flowKey).getAsJsonObject());
        String urlString = state.getPageObject().get("url").getAsString();
        getWebDriver().get(urlString);
        LOGGER.info(String.format("\n\tNavigate to the website: %s\n\t", urlString));
    }

    @Then("^I refresh the page$")
    public void iRefreshThePage() {
        String url = getWebDriver().getCurrentUrl();
        getWebDriver().navigate().refresh();
        LOGGER.info(String.format("\n\tThe page [%s] has been refreshed.\n\t", url));
    }

    @Then("^I open new tab$")
    public void iOpenNewTab() {
        ((JavascriptExecutor) getWebDriver()).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<>(getWebDriver().getWindowHandles());
        getWebDriver().switchTo().window(tabs.get(1));
        LOGGER.info("\n\tThe new tab is opened\n\t");
    }

    @Then("^I close the tab$")
    public void iCloseTheTab() {
        ((JavascriptExecutor) getWebDriver()).executeScript("window.close()");
        LOGGER.info("\n\tThe tab is closed\n\t");
        ArrayList<String> tabs = new ArrayList<>(getWebDriver().getWindowHandles());
        getWebDriver().switchTo().window(tabs.get(tabs.size() - 1));
    }

    @When("^I navigate back$")
    public void iNavigateBack() {
        getWebDriver().navigate().back();
        LOGGER.info("\n\tNavigated back\n\t");
    }

    @When("^I navigate forward$")
    public void iNavigateForward() {
        getWebDriver().navigate().forward();
        LOGGER.info("\n\tNavigated forward\n\t");
    }

    @When("^I navigate to url \"([^\"]*)\"$")
    public void iNavigateToUrl(String url) {
        getWebDriver().get(url);
        LOGGER.info(String.format("\n\tNavigated to URL: [%s]\n\t", url));
    }

    @When("^I open the url \"([^\"]*)\" in new tab$")
    public void iOpenUrlNewTab(String pageKey) {
        ((JavascriptExecutor) getWebDriver()).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<>(getWebDriver().getWindowHandles());
        getWebDriver().switchTo().window(tabs.get(1));
        getWebDriver().get(pageKey);
        LOGGER.info("\n\tThe URL [" + pageKey + "] opened in new tab\n\t");
    }

    @When("^I wait for page$")
    public void iWaitForPage() {
        getWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        LOGGER.info("\n\tWait for page\n\t");
    }

    @Then("^I switch the iFrame \"([^\"]*)\"$")
    public void iSwitchFrame(String frameIdentifier) {
        WebElement iframe = getWebDriver().findElement(By.xpath(frameIdentifier));
        getWebDriver().switchTo().frame(iframe);
        LOGGER.info(String.format("\n\tiFrame switched: %s\n\t", frameIdentifier));
    }

    @Then("^I switch to default content$")
    public void iSwitchToDefaultContent() {
        getWebDriver().switchTo().defaultContent();
        LOGGER.info("\n\tSwitched to default content\n\t");
    }
}
