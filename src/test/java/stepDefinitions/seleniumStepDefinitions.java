package stepDefinitions;


import com.google.gson.JsonObject;
import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import org.junit.Assert;
import org.openqa.selenium.*;
import static resources.logger.LoggerManagement.LOGGER;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import resources.config.driver.selectDecision;
import resources.config.utils.jsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class seleniumStepDefinitions {

    private WebDriver webDriver;
    private String osValue = System.getProperty("os.name");
    private JsonObject pageObject;
    private static int sceneriosCounter = 0;
    private static int failedSceneriosCounter = 0;
    private By by;

    public seleniumStepDefinitions(){


    }

    @Before
    public void beforeScenerio(Scenario scenario){
        LOGGER.info(String.format("\n\n\t[%d] > Scenerio [%s] started\t",++sceneriosCounter, scenario.getName() ));
    }
    @After
    public void afterScenerio(Scenario scenerio){
        if(scenerio.isFailed()){
            ++failedSceneriosCounter;
        }else
            webDriver.quit();

        String result = scenerio.isFailed() ? "with errors" : "succesfully";
        LOGGER.info(String.format("\n\t[%d] > Scenerio [%s] finished %s\t", sceneriosCounter,scenerio.getName(),result));
        LOGGER.info(String.format("\n\t%d of %d scenerios failed so far \t ",failedSceneriosCounter, sceneriosCounter));
    }

    @Given("^I use (\\w+(?: \\w+)*) driver")
    public void useDriver(String browserKey){
        webDriver = config.browserManager.browser(osValue,browserKey);
        LOGGER.info(String.format("\n\tDriver is: %s\n\t", browserKey));

    }


    @When("^I open (\\w+(?: \\w+)*) page$")
    public void i_open_page(String flowKey) {

        JsonObject jsonObject = jsonParser.main("pages");
        this.pageObject = jsonObject.get(flowKey).getAsJsonObject();
        String urlString = this.pageObject.get("url").getAsString();

        webDriver.get(urlString);

        LOGGER.info(String.format("\n\tNavigate to the website: %s\n\t", urlString));

    }

    @Then("^I refresh the page$")
    public void i_refresh_the_page()  {
        String URL = webDriver.getCurrentUrl();
        webDriver.navigate().refresh();
        LOGGER.info(String.format("\n\t The page [%s] has been refreshed.\n\t ",URL));
    }
    @Then("^I see webpage title equals \"([^\"]*)\"$")
    public void webpageTitleEquals(String expectedHeader){
        String currentHeader = webDriver.getTitle();

        if(expectedHeader.equals(currentHeader)) {
            LOGGER.info(String.format("\n\tThe webpage title is [%s] as expected [%s]\n\t", currentHeader, expectedHeader));
        }

    }

    @Then("^I see webpage title contains \"([^\"]*)\"$")
    public void webpageTitleContains(String expectedHeader){
        String currentHeader = webDriver.getTitle();

        if(expectedHeader.contains(currentHeader)){
            LOGGER.info(String.format("\n\tThe webpage title is [%s] contains [%s]\n\t", currentHeader, expectedHeader));
        }
    }

    @Then("^I see webpage title does not equal \"([^\"]*)\"$")
    public void webpageTitleNotEqual(String expectedHeader){
        String currentHeader = webDriver.getTitle();

        if(!expectedHeader.equals(currentHeader))
            throw new java.lang.AssertionError(String.format("\n\tThe webpage title is NOT [%s] as expected [%s]\n\t", currentHeader, expectedHeader));

    }

    @Then("^I see webpage title does not contain \"([^\"]*)\"$")
    public void webpageTitleNotContain(String expectedHeader){
        String currentHeader = webDriver.getTitle();


        if(!expectedHeader.contains(currentHeader)){
            throw new java.lang.AssertionError(String.format("\n\tThe webpage title is NOT [%s] as expected [%s]\n\t", currentHeader, expectedHeader));
        }
    }


    @And("^I see text$")
    public void iSeeText(DataTable table) {

        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get("mainPanel").getAsString();
        this.by = selectDecision.main(this.by, "xpath", pageElement);

       /* webDriver.findElement(
                            new ByAll(By.className(pageElement),
                                    By.cssSelector(pageElement),
                                    By.id(pageElement),
                                    By.linkText(pageElement),
                                    By.name(pageElement),
                                    By.xpath(pageElement),
                                    By.tagName(pageElement),
                                    By.partialLinkText(pageElement)));*/

        for ( DataTableRow row : table.getGherkinRows() ) {

            String key = row.getCells().get(0);

            boolean isThere = webDriver.getPageSource().contains(key);
            if (isThere) {
                LOGGER.info(String.format("\n\tThe text is in the page: %s\n\t", key));
            } else {
                String temp = webDriver.getPageSource();
                System.out.println(temp);
                throw new java.lang.AssertionError(String.format("\n\tThe text is NOT in the page: %s\n\t", key));
            }

        }
    }



   @Then("^I open new tab$")
    public void i_open_new_tab(){

       ((JavascriptExecutor)webDriver).executeScript("window.open()");
       ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
       webDriver.switchTo().window(tabs.get(1));
       LOGGER.info(String.format("\n\t The new tab is opened\n\t"));

   }

    @Then("^I close the tab$")
    public void i_close_the_tab() {

        ((JavascriptExecutor)webDriver).executeScript("window.close()");
        LOGGER.info(String.format("\n\t The tab is closed\n\t"));
        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(tabs.size() - 1));

    }

    @Then("^I see (\\w+(?: \\w+)*) equals to \"([^\"]*)\"$")
    public void iSeeElement(String pageKey, String valueKey) {

        JsonObject pageElementObject = pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        String element = webDriver.findElement(By.xpath(pageElement)).getText();
        Assert.assertEquals(valueKey,element);
        LOGGER.info(String.format("\n\tCheck web element: %s\n\t", pageKey));
    }

    @Then("^I mouse hover to element (\\w+(?: \\w+)*)$") //sor
    public void iMouseHover(WebElement element){
        Actions action = new Actions(webDriver);
        action.moveToElement(element).perform();
        LOGGER.info(String.format("\n\tMouse hover to element: %s\n\t", element));

    }

    @Then("I mouse hover to element (\\w+(?: \\w+)*) and click")
    public void iMouseClick(WebElement element){
        Actions action =new  Actions(webDriver);
        action.moveToElement(element).click(element).build().perform();
        LOGGER.info(String.format("\n\tMouse hover and click to element: %s\n\t", element));


    }

    @When("^I wait for page$")
    public void iWaitForPage() {

        webDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        LOGGER.info("\n\tWait for page\n\t");

    }


    @Then("^I switch the iFrame \"([^\"]*)\"$")
    public void iSwitchFrame() {
        WebElement iframe = webDriver.findElement(By.tagName("iframe"));
        webDriver.switchTo().frame(iframe);
        LOGGER.info(String.format("\n\tiFrame switched: %s\n\t", iframe));

    }





    @Then("^I fill by (\\w+(?: \\w+)*)$")
    public void iFillBy(String selectKey, DataTable table) {

        for ( DataTableRow row : table.getGherkinRows() ) {

            String key = row.getCells().get(0);
            String value = row.getCells().get(1);

            JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
            String pageElement = pageElementObject.get(key).getAsString();

            this.by = selectDecision.main(this.by, selectKey, pageElement);

            webDriver.findElements( this.by ).clear();
            webDriver.findElement( this.by ).sendKeys( value );

            LOGGER.info(String.format("\n\tFilling the key: [%s] \t with the value: [%s]\n\t", key, value));

        }

    }

    @When("^I open the url \"([^\"]*)\" in new tab$")
    public void iOpenUrlNewTab(String pageKey){
        ((JavascriptExecutor)webDriver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(1));
        webDriver.get(pageKey);
        LOGGER.info("\n\tTh URL [" + pageKey + "] \t opened in new tab\n\t");


    }

    @When("^I click (\\w+(?: \\w+)*), link: (\\w+(?: \\w+)*) opened in new tab$")
    public void iClickedNewTab(String pageKey,String selectKey){
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = selectDecision.main(this.by, selectKey, pageElement);
        WebElement element = this.webDriver.findElement(by);
        Actions action = new Actions(webDriver);
        action.keyDown(Keys.COMMAND).keyDown(Keys.SHIFT).click(element).keyUp(Keys.CONTROL).keyUp(Keys.SHIFT).build().perform();

        LOGGER.info(String.format("\n\tClicking link and opened in new tab %s\n\t", pageKey));

    }

    @Then("^I select (\\w+(?: \\w+)*) dropdown option on element (\\w+(?: \\w+)*)$")
    public void iSelectDropdownOption(String option, String element){
        WebElement dropdown = webDriver.findElement(By.tagName("option"));
        Select dd = new Select(dropdown);
        dd.selectByVisibleText(option);
        dd.selectByValue(element);
    }

















}
