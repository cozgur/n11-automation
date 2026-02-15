package com.company.qa.tests.steps;

import com.google.gson.JsonObject;
import com.company.qa.core.driver.BrowserManager;
import com.company.qa.core.driver.SelectDecision;
import com.company.qa.core.util.JsonParser;
import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.company.qa.core.util.LogManager.LOGGER;

public class WebStepDefinitions {

    private WebDriver webDriver;
    private String osValue = System.getProperty("os.name");
    private JsonObject pageObject;
    private By by;

    @Given("^I use (\\w+(?: \\w+)*) driver")
    public void useDriver(String browserKey) {
        webDriver = BrowserManager.createBrowser(osValue, browserKey);
        LOGGER.info(String.format("\n\tDriver is: %s\n\t", browserKey));
    }

    @When("^I open (\\w+(?: \\w+)*) page$")
    public void iOpenPage(String flowKey) {
        JsonObject jsonObject = JsonParser.parse("pages");
        this.pageObject = jsonObject.get(flowKey).getAsJsonObject();
        String urlString = this.pageObject.get("url").getAsString();
        webDriver.get(urlString);
        LOGGER.info(String.format("\n\tNavigate to the website: %s\n\t", urlString));
    }

    @Then("^I refresh the page$")
    public void iRefreshThePage() {
        String url = webDriver.getCurrentUrl();
        webDriver.navigate().refresh();
        LOGGER.info(String.format("\n\tThe page [%s] has been refreshed.\n\t", url));
    }

    @Then("^I see webpage title equals \"([^\"]*)\"$")
    public void webpageTitleEquals(String expectedHeader) {
        String currentHeader = webDriver.getTitle();
        Assert.assertEquals(String.format("Expected webpage title [%s] but got [%s]", expectedHeader, currentHeader),
                expectedHeader, currentHeader);
        LOGGER.info(String.format("\n\tThe webpage title is [%s] as expected [%s]\n\t", currentHeader, expectedHeader));
    }

    @Then("^I see webpage title contains \"([^\"]*)\"$")
    public void webpageTitleContains(String expectedHeader) {
        String currentHeader = webDriver.getTitle();
        Assert.assertTrue(String.format("Expected webpage title [%s] to contain [%s]", currentHeader, expectedHeader),
                currentHeader.contains(expectedHeader));
        LOGGER.info(String.format("\n\tThe webpage title [%s] contains [%s]\n\t", currentHeader, expectedHeader));
    }

    @Then("^I see webpage title does not equal \"([^\"]*)\"$")
    public void webpageTitleNotEqual(String expectedHeader) {
        String currentHeader = webDriver.getTitle();
        Assert.assertNotEquals(String.format("Expected webpage title to NOT equal [%s] but it does", expectedHeader),
                expectedHeader, currentHeader);
        LOGGER.info(String.format("\n\tThe webpage title [%s] does not equal [%s] as expected\n\t", currentHeader, expectedHeader));
    }

    @Then("^I see webpage title does not contain \"([^\"]*)\"$")
    public void webpageTitleNotContain(String expectedHeader) {
        String currentHeader = webDriver.getTitle();
        Assert.assertFalse(String.format("Expected webpage title [%s] to NOT contain [%s] but it does", currentHeader, expectedHeader),
                currentHeader.contains(expectedHeader));
        LOGGER.info(String.format("\n\tThe webpage title [%s] does not contain [%s] as expected\n\t", currentHeader, expectedHeader));
    }

    @And("^I see text$")
    public void iSeeText(DataTable table) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get("mainPanel").getAsString();
        this.by = SelectDecision.resolve("xpath", pageElement);

        for (DataTableRow row : table.getGherkinRows()) {
            String key = row.getCells().get(0);
            boolean isThere = webDriver.getPageSource().contains(key);
            if (isThere) {
                LOGGER.info(String.format("\n\tThe text is in the page: %s\n\t", key));
            } else {
                throw new AssertionError(String.format("\n\tThe text is NOT in the page: %s\n\t", key));
            }
        }
    }

    @Then("^I open new tab$")
    public void iOpenNewTab() {
        ((JavascriptExecutor) webDriver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(1));
        LOGGER.info("\n\tThe new tab is opened\n\t");
    }

    @Then("^I close the tab$")
    public void iCloseTheTab() {
        ((JavascriptExecutor) webDriver).executeScript("window.close()");
        LOGGER.info("\n\tThe tab is closed\n\t");
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    @Then("^I see (\\w+(?: \\w+)*) equals to \"([^\"]*)\"$")
    public void iSeeElement(String pageKey, String valueKey) {
        JsonObject pageElementObject = pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        String element = webDriver.findElement(By.xpath(pageElement)).getText();
        Assert.assertEquals(valueKey, element);
        LOGGER.info(String.format("\n\tCheck web element: %s\n\t", pageKey));
    }

    @Then("^I mouse hover to element (\\w+(?: \\w+)*)$")
    public void iMouseHover(WebElement element) {
        Actions action = new Actions(webDriver);
        action.moveToElement(element).perform();
        LOGGER.info(String.format("\n\tMouse hover to element: %s\n\t", element));
    }

    @Then("I mouse hover to element (\\w+(?: \\w+)*) and click")
    public void iMouseClick(WebElement element) {
        Actions action = new Actions(webDriver);
        action.moveToElement(element).click(element).build().perform();
        LOGGER.info(String.format("\n\tMouse hover and click to element: %s\n\t", element));
    }

    @When("^I wait for page$")
    public void iWaitForPage() {
        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        LOGGER.info("\n\tWait for page\n\t");
    }

    @Then("^I switch the iFrame \"([^\"]*)\"$")
    public void iSwitchFrame(String frameIdentifier) {
        WebElement iframe = webDriver.findElement(By.xpath(frameIdentifier));
        webDriver.switchTo().frame(iframe);
        LOGGER.info(String.format("\n\tiFrame switched: %s\n\t", frameIdentifier));
    }

    @Then("^I fill by (\\w+(?: \\w+)*)$")
    public void iFillBy(String selectKey, DataTable table) {
        for (DataTableRow row : table.getGherkinRows()) {
            String key = row.getCells().get(0);
            String value = row.getCells().get(1);
            JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
            String pageElement = pageElementObject.get(key).getAsString();
            this.by = SelectDecision.resolve(selectKey, pageElement);
            webDriver.findElements(this.by).clear();
            webDriver.findElement(this.by).sendKeys(value);
            LOGGER.info(String.format("\n\tFilling the key: [%s] \t with the value: [%s]\n\t", key, value));
        }
    }

    @When("^I open the url \"([^\"]*)\" in new tab$")
    public void iOpenUrlNewTab(String pageKey) {
        ((JavascriptExecutor) webDriver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(1));
        webDriver.get(pageKey);
        LOGGER.info("\n\tThe URL [" + pageKey + "] opened in new tab\n\t");
    }

    @When("^I click (\\w+(?: \\w+)*), link: (\\w+(?: \\w+)*) opened in new tab$")
    public void iClickedNewTab(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        WebElement element = this.webDriver.findElement(by);
        Actions action = new Actions(webDriver);
        action.keyDown(Keys.COMMAND).keyDown(Keys.SHIFT).click(element).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();
        LOGGER.info(String.format("\n\tClicking link and opened in new tab %s\n\t", pageKey));
    }

    @Then("^I select (\\w+(?: \\w+)*) dropdown option on element (\\w+(?: \\w+)*)$")
    public void iSelectDropdownOption(String option, String element) {
        WebElement dropdown = webDriver.findElement(By.tagName("option"));
        Select dd = new Select(dropdown);
        dd.selectByVisibleText(option);
        dd.selectByValue(element);
    }

    // --- Click actions ---

    @Then("^I click by (\\w+(?: \\w+)*)$")
    public void iClickBy(String selectKey, DataTable table) {
        for (DataTableRow row : table.getGherkinRows()) {
            String key = row.getCells().get(0);
            JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
            String pageElement = pageElementObject.get(key).getAsString();
            this.by = SelectDecision.resolve(selectKey, pageElement);
            webDriver.findElement(this.by).click();
            LOGGER.info(String.format("\n\tClicked element: [%s]\n\t", key));
        }
    }

    @Then("^I click element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iClickElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        webDriver.findElement(this.by).click();
        LOGGER.info(String.format("\n\tClicked element: [%s]\n\t", pageKey));
    }

    @Then("^I double click element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iDoubleClickElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        WebElement element = webDriver.findElement(this.by);
        new Actions(webDriver).doubleClick(element).perform();
        LOGGER.info(String.format("\n\tDouble clicked element: [%s]\n\t", pageKey));
    }

    @Then("^I right click element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iRightClickElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        WebElement element = webDriver.findElement(this.by);
        new Actions(webDriver).contextClick(element).perform();
        LOGGER.info(String.format("\n\tRight clicked element: [%s]\n\t", pageKey));
    }

    // --- Navigation ---

    @When("^I navigate back$")
    public void iNavigateBack() {
        webDriver.navigate().back();
        LOGGER.info("\n\tNavigated back\n\t");
    }

    @When("^I navigate forward$")
    public void iNavigateForward() {
        webDriver.navigate().forward();
        LOGGER.info("\n\tNavigated forward\n\t");
    }

    @When("^I navigate to url \"([^\"]*)\"$")
    public void iNavigateToUrl(String url) {
        webDriver.get(url);
        LOGGER.info(String.format("\n\tNavigated to URL: [%s]\n\t", url));
    }

    // --- URL assertions ---

    @Then("^I see current url equals \"([^\"]*)\"$")
    public void iSeeCurrentUrlEquals(String expectedUrl) {
        String currentUrl = webDriver.getCurrentUrl();
        Assert.assertEquals(String.format("Expected URL [%s] but got [%s]", expectedUrl, currentUrl),
                expectedUrl, currentUrl);
        LOGGER.info(String.format("\n\tCurrent URL [%s] equals expected [%s]\n\t", currentUrl, expectedUrl));
    }

    @Then("^I see current url contains \"([^\"]*)\"$")
    public void iSeeCurrentUrlContains(String expectedText) {
        String currentUrl = webDriver.getCurrentUrl();
        Assert.assertTrue(String.format("Expected URL [%s] to contain [%s]", currentUrl, expectedText),
                currentUrl.contains(expectedText));
        LOGGER.info(String.format("\n\tCurrent URL [%s] contains [%s]\n\t", currentUrl, expectedText));
    }

    // --- Element state assertions ---

    @Then("^I see element (\\w+(?: \\w+)*) is displayed by (\\w+(?: \\w+)*)$")
    public void iSeeElementIsDisplayed(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        boolean isDisplayed = webDriver.findElement(this.by).isDisplayed();
        Assert.assertTrue(String.format("Expected element [%s] to be displayed but it is not", pageKey), isDisplayed);
        LOGGER.info(String.format("\n\tElement [%s] is displayed\n\t", pageKey));
    }

    @Then("^I see element (\\w+(?: \\w+)*) is not displayed by (\\w+(?: \\w+)*)$")
    public void iSeeElementIsNotDisplayed(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        List<WebElement> elements = webDriver.findElements(this.by);
        boolean isNotDisplayed = elements.isEmpty() || !elements.get(0).isDisplayed();
        Assert.assertTrue(String.format("Expected element [%s] to NOT be displayed but it is", pageKey), isNotDisplayed);
        LOGGER.info(String.format("\n\tElement [%s] is not displayed\n\t", pageKey));
    }

    @Then("^I see element (\\w+(?: \\w+)*) is enabled by (\\w+(?: \\w+)*)$")
    public void iSeeElementIsEnabled(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        boolean isEnabled = webDriver.findElement(this.by).isEnabled();
        Assert.assertTrue(String.format("Expected element [%s] to be enabled but it is not", pageKey), isEnabled);
        LOGGER.info(String.format("\n\tElement [%s] is enabled\n\t", pageKey));
    }

    @Then("^I see element (\\w+(?: \\w+)*) is disabled by (\\w+(?: \\w+)*)$")
    public void iSeeElementIsDisabled(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        boolean isEnabled = webDriver.findElement(this.by).isEnabled();
        Assert.assertFalse(String.format("Expected element [%s] to be disabled but it is enabled", pageKey), isEnabled);
        LOGGER.info(String.format("\n\tElement [%s] is disabled\n\t", pageKey));
    }

    @Then("^I see element (\\w+(?: \\w+)*) attribute \"([^\"]*)\" equals \"([^\"]*)\" by (\\w+(?: \\w+)*)$")
    public void iSeeElementAttributeEquals(String pageKey, String attribute, String expectedValue, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        String actualValue = webDriver.findElement(this.by).getAttribute(attribute);
        Assert.assertEquals(String.format("Expected attribute [%s] of element [%s] to be [%s] but got [%s]",
                attribute, pageKey, expectedValue, actualValue), expectedValue, actualValue);
        LOGGER.info(String.format("\n\tElement [%s] attribute [%s] equals [%s]\n\t", pageKey, attribute, expectedValue));
    }

    @Then("^I see element count (\\w+(?: \\w+)*) is (\\d+) by (\\w+(?: \\w+)*)$")
    public void iSeeElementCount(String pageKey, int expectedCount, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        int actualCount = webDriver.findElements(this.by).size();
        Assert.assertEquals(String.format("Expected [%d] elements for [%s] but found [%d]",
                expectedCount, pageKey, actualCount), expectedCount, actualCount);
        LOGGER.info(String.format("\n\tElement [%s] count is [%d] as expected\n\t", pageKey, expectedCount));
    }

    // --- Alert handling ---

    @Then("^I accept the alert$")
    public void iAcceptAlert() {
        webDriver.switchTo().alert().accept();
        LOGGER.info("\n\tAlert accepted\n\t");
    }

    @Then("^I dismiss the alert$")
    public void iDismissAlert() {
        webDriver.switchTo().alert().dismiss();
        LOGGER.info("\n\tAlert dismissed\n\t");
    }

    @Then("^I see alert text equals \"([^\"]*)\"$")
    public void iSeeAlertTextEquals(String expectedText) {
        String alertText = webDriver.switchTo().alert().getText();
        Assert.assertEquals(String.format("Expected alert text [%s] but got [%s]", expectedText, alertText),
                expectedText, alertText);
        LOGGER.info(String.format("\n\tAlert text is [%s] as expected\n\t", alertText));
    }

    @Then("^I type \"([^\"]*)\" into alert$")
    public void iTypeIntoAlert(String text) {
        webDriver.switchTo().alert().sendKeys(text);
        LOGGER.info(String.format("\n\tTyped [%s] into alert\n\t", text));
    }

    // --- Keyboard actions ---

    @Then("^I press enter on element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iPressEnterOnElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        webDriver.findElement(this.by).sendKeys(Keys.ENTER);
        LOGGER.info(String.format("\n\tPressed ENTER on element: [%s]\n\t", pageKey));
    }

    @Then("^I press escape$")
    public void iPressEscape() {
        new Actions(webDriver).sendKeys(Keys.ESCAPE).perform();
        LOGGER.info("\n\tPressed ESCAPE\n\t");
    }

    @Then("^I press tab on element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iPressTabOnElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        webDriver.findElement(this.by).sendKeys(Keys.TAB);
        LOGGER.info(String.format("\n\tPressed TAB on element: [%s]\n\t", pageKey));
    }

    // --- Scroll and clear ---

    @Then("^I scroll to element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iScrollToElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        WebElement element = webDriver.findElement(this.by);
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
        LOGGER.info(String.format("\n\tScrolled to element: [%s]\n\t", pageKey));
    }

    @Then("^I clear element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iClearElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        webDriver.findElement(this.by).clear();
        LOGGER.info(String.format("\n\tCleared element: [%s]\n\t", pageKey));
    }

    // --- iFrame ---

    @Then("^I switch to default content$")
    public void iSwitchToDefaultContent() {
        webDriver.switchTo().defaultContent();
        LOGGER.info("\n\tSwitched to default content\n\t");
    }

    // --- Form submission ---

    @Then("^I submit element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iSubmitElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        webDriver.findElement(this.by).submit();
        LOGGER.info(String.format("\n\tSubmitted form via element: [%s]\n\t", pageKey));
    }
}
