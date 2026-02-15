package stepDefinitions;

import com.google.gson.JsonObject;
import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.formatter.model.DataTableRow;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import resources.config.driver.mobileDriverManager;
import resources.config.driver.selectDecision;
import resources.config.utils.jsonParser;

import java.util.Set;

import static resources.logger.LoggerManagement.LOGGER;

public class mobileStepDefinitions {

    private AppiumDriver<MobileElement> mobileDriver;
    private JsonObject pageObject;
    private By by;

    // --- Driver setup ---

    @Given("^I use (android|ios) mobile driver on device \"([^\"]*)\" version \"([^\"]*)\"$")
    public void useMobileDriver(String platform, String deviceName, String platformVersion) {
        mobileDriver = mobileDriverManager.createDriver(platform, deviceName, platformVersion);
        LOGGER.info(String.format("\n\tMobile driver created: [%s] device [%s] version [%s]\n\t",
                platform, deviceName, platformVersion));
    }

    @Given("^I use (android|ios) mobile driver on device \"([^\"]*)\" version \"([^\"]*)\" with app \"([^\"]*)\"$")
    public void useMobileDriverWithApp(String platform, String deviceName, String platformVersion, String app) {
        mobileDriver = mobileDriverManager.createDriverWithApp(platform, deviceName, platformVersion, app);
        LOGGER.info(String.format("\n\tMobile driver created: [%s] device [%s] app [%s]\n\t",
                platform, deviceName, app));
    }

    @Given("^I use (android|ios) mobile driver on device \"([^\"]*)\" version \"([^\"]*)\" at \"([^\"]*)\"$")
    public void useMobileDriverAtUrl(String platform, String deviceName, String platformVersion, String appiumUrl) {
        mobileDriver = mobileDriverManager.createDriver(platform, deviceName, platformVersion, appiumUrl);
        LOGGER.info(String.format("\n\tMobile driver created: [%s] at [%s]\n\t", platform, appiumUrl));
    }

    @When("^I open mobile (\\w+(?: \\w+)*) page$")
    public void iOpenMobilePage(String flowKey) {
        JsonObject jsonObject = jsonParser.main("pages");
        this.pageObject = jsonObject.get(flowKey).getAsJsonObject();
        String urlString = this.pageObject.get("url").getAsString();
        mobileDriver.get(urlString);
        LOGGER.info(String.format("\n\tMobile navigated to: %s\n\t", urlString));
    }

    @After("@mobile")
    public void afterMobileScenario(Scenario scenario) {
        if (mobileDriver != null) {
            mobileDriver.quit();
            LOGGER.info("\n\tMobile driver quit\n\t");
        }
    }

    // --- App lifecycle ---

    @When("^I launch the mobile app$")
    public void iLaunchApp() {
        mobileDriver.launchApp();
        LOGGER.info("\n\tMobile app launched\n\t");
    }

    @When("^I close the mobile app$")
    public void iCloseApp() {
        mobileDriver.closeApp();
        LOGGER.info("\n\tMobile app closed\n\t");
    }

    @When("^I reset the mobile app$")
    public void iResetApp() {
        mobileDriver.resetApp();
        LOGGER.info("\n\tMobile app reset\n\t");
    }

    @When("^I send the mobile app to background for (\\d+) seconds$")
    public void iBackgroundApp(int seconds) {
        mobileDriver.runAppInBackground(seconds);
        LOGGER.info(String.format("\n\tMobile app sent to background for [%d] seconds\n\t", seconds));
    }

    // --- Touch gestures ---

    @Then("^I tap element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iTapElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = selectDecision.main(this.by, selectKey, pageElement);
        WebElement element = mobileDriver.findElement(this.by);
        new TouchAction(mobileDriver).tap(element).perform();
        LOGGER.info(String.format("\n\tTapped element: [%s]\n\t", pageKey));
    }

    @Then("^I long press element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iLongPressElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = selectDecision.main(this.by, selectKey, pageElement);
        WebElement element = mobileDriver.findElement(this.by);
        new TouchAction(mobileDriver).longPress(element).release().perform();
        LOGGER.info(String.format("\n\tLong pressed element: [%s]\n\t", pageKey));
    }

    @Then("^I tap on coordinates (\\d+) (\\d+)$")
    public void iTapOnCoordinates(int x, int y) {
        new TouchAction(mobileDriver).tap(x, y).perform();
        LOGGER.info(String.format("\n\tTapped on coordinates: [%d, %d]\n\t", x, y));
    }

    @Then("^I long press on coordinates (\\d+) (\\d+)$")
    public void iLongPressOnCoordinates(int x, int y) {
        new TouchAction(mobileDriver).longPress(x, y).release().perform();
        LOGGER.info(String.format("\n\tLong pressed on coordinates: [%d, %d]\n\t", x, y));
    }

    @Then("^I swipe (up|down|left|right)$")
    public void iSwipe(String direction) {
        Dimension size = mobileDriver.manage().window().getSize();
        int centerX = size.width / 2;
        int centerY = size.height / 2;

        int startX, startY, endX, endY;

        switch (direction.toLowerCase()) {
            case "up":
                startX = centerX;
                startY = (int) (size.height * 0.7);
                endX = centerX;
                endY = (int) (size.height * 0.3);
                break;
            case "down":
                startX = centerX;
                startY = (int) (size.height * 0.3);
                endX = centerX;
                endY = (int) (size.height * 0.7);
                break;
            case "left":
                startX = (int) (size.width * 0.8);
                startY = centerY;
                endX = (int) (size.width * 0.2);
                endY = centerY;
                break;
            case "right":
                startX = (int) (size.width * 0.2);
                startY = centerY;
                endX = (int) (size.width * 0.8);
                endY = centerY;
                break;
            default:
                throw new IllegalArgumentException("Invalid swipe direction: " + direction);
        }

        mobileDriver.swipe(startX, startY, endX, endY, 500);
        LOGGER.info(String.format("\n\tSwiped [%s]\n\t", direction));
    }

    @Then("^I swipe (up|down|left|right) (\\d+) times$")
    public void iSwipeMultiple(String direction, int times) {
        for (int i = 0; i < times; i++) {
            iSwipe(direction);
        }
        LOGGER.info(String.format("\n\tSwiped [%s] [%d] times\n\t", direction, times));
    }

    @Then("^I pinch element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iPinchElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = selectDecision.main(this.by, selectKey, pageElement);
        WebElement element = mobileDriver.findElement(this.by);
        mobileDriver.pinch(element);
        LOGGER.info(String.format("\n\tPinched element: [%s]\n\t", pageKey));
    }

    @Then("^I zoom element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iZoomElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = selectDecision.main(this.by, selectKey, pageElement);
        WebElement element = mobileDriver.findElement(this.by);
        mobileDriver.zoom(element);
        LOGGER.info(String.format("\n\tZoomed element: [%s]\n\t", pageKey));
    }

    // --- Device actions ---

    @Then("^I hide the keyboard$")
    public void iHideKeyboard() {
        mobileDriver.hideKeyboard();
        LOGGER.info("\n\tKeyboard hidden\n\t");
    }

    @Then("^I rotate to (landscape|portrait)$")
    public void iRotate(String orientation) {
        if (orientation.equalsIgnoreCase("landscape")) {
            mobileDriver.rotate(ScreenOrientation.LANDSCAPE);
        } else {
            mobileDriver.rotate(ScreenOrientation.PORTRAIT);
        }
        LOGGER.info(String.format("\n\tRotated to [%s]\n\t", orientation));
    }

    @Then("^I press the back button$")
    public void iPressBackButton() {
        if (mobileDriver instanceof AndroidDriver) {
            ((AndroidDriver) mobileDriver).pressKeyCode(AndroidKeyCode.BACK);
            LOGGER.info("\n\tPressed back button\n\t");
        } else {
            LOGGER.info("\n\tBack button is only available on Android\n\t");
        }
    }

    @Then("^I press the home button$")
    public void iPressHomeButton() {
        if (mobileDriver instanceof AndroidDriver) {
            ((AndroidDriver) mobileDriver).pressKeyCode(AndroidKeyCode.HOME);
            LOGGER.info("\n\tPressed home button\n\t");
        } else {
            LOGGER.info("\n\tHome button is only available on Android\n\t");
        }
    }

    @Then("^I press the enter key$")
    public void iPressEnterKey() {
        if (mobileDriver instanceof AndroidDriver) {
            ((AndroidDriver) mobileDriver).pressKeyCode(AndroidKeyCode.ENTER);
            LOGGER.info("\n\tPressed enter key\n\t");
        } else {
            LOGGER.info("\n\tAndroid key code is only available on Android\n\t");
        }
    }

    @Then("^I lock the device$")
    public void iLockDevice() {
        mobileDriver.lockScreen(0);
        LOGGER.info("\n\tDevice locked\n\t");
    }

    @Then("^I lock the device for (\\d+) seconds$")
    public void iLockDeviceForSeconds(int seconds) {
        mobileDriver.lockScreen(seconds);
        LOGGER.info(String.format("\n\tDevice locked for [%d] seconds\n\t", seconds));
    }

    // --- Context switching ---

    @Then("^I switch to native context$")
    public void iSwitchToNativeContext() {
        mobileDriver.context("NATIVE_APP");
        LOGGER.info("\n\tSwitched to NATIVE_APP context\n\t");
    }

    @Then("^I switch to webview context$")
    public void iSwitchToWebviewContext() {
        Set<String> contexts = mobileDriver.getContextHandles();
        for (String context : contexts) {
            if (context.contains("WEBVIEW")) {
                mobileDriver.context(context);
                LOGGER.info(String.format("\n\tSwitched to context: [%s]\n\t", context));
                return;
            }
        }
        throw new RuntimeException("No WEBVIEW context found. Available contexts: " + contexts);
    }

    @Then("^I switch to context \"([^\"]*)\"$")
    public void iSwitchToContext(String contextName) {
        mobileDriver.context(contextName);
        LOGGER.info(String.format("\n\tSwitched to context: [%s]\n\t", contextName));
    }

    // --- Mobile element interaction ---

    @Then("^I type \"([^\"]*)\" on mobile element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iTypeOnMobileElement(String text, String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = selectDecision.main(this.by, selectKey, pageElement);
        mobileDriver.findElement(this.by).sendKeys(text);
        LOGGER.info(String.format("\n\tTyped [%s] on mobile element: [%s]\n\t", text, pageKey));
    }

    @Then("^I clear mobile element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iClearMobileElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = selectDecision.main(this.by, selectKey, pageElement);
        mobileDriver.findElement(this.by).clear();
        LOGGER.info(String.format("\n\tCleared mobile element: [%s]\n\t", pageKey));
    }

    @Then("^I fill mobile by (\\w+(?: \\w+)*)$")
    public void iFillMobileBy(String selectKey, DataTable table) {
        for (DataTableRow row : table.getGherkinRows()) {
            String key = row.getCells().get(0);
            String value = row.getCells().get(1);

            JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
            String pageElement = pageElementObject.get(key).getAsString();
            this.by = selectDecision.main(this.by, selectKey, pageElement);

            mobileDriver.findElement(this.by).clear();
            mobileDriver.findElement(this.by).sendKeys(value);

            LOGGER.info(String.format("\n\tMobile filling key: [%s] with value: [%s]\n\t", key, value));
        }
    }

    @Then("^I tap mobile by (\\w+(?: \\w+)*)$")
    public void iTapMobileBy(String selectKey, DataTable table) {
        for (DataTableRow row : table.getGherkinRows()) {
            String key = row.getCells().get(0);

            JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
            String pageElement = pageElementObject.get(key).getAsString();
            this.by = selectDecision.main(this.by, selectKey, pageElement);

            WebElement element = mobileDriver.findElement(this.by);
            new TouchAction(mobileDriver).tap(element).perform();

            LOGGER.info(String.format("\n\tMobile tapped element: [%s]\n\t", key));
        }
    }

    // --- Find by accessibility ID ---

    @Then("^I tap element with accessibility id \"([^\"]*)\"$")
    public void iTapByAccessibilityId(String accessibilityId) {
        WebElement element = mobileDriver.findElementByAccessibilityId(accessibilityId);
        new TouchAction(mobileDriver).tap(element).perform();
        LOGGER.info(String.format("\n\tTapped element with accessibility ID: [%s]\n\t", accessibilityId));
    }

    @Then("^I type \"([^\"]*)\" on element with accessibility id \"([^\"]*)\"$")
    public void iTypeByAccessibilityId(String text, String accessibilityId) {
        mobileDriver.findElementByAccessibilityId(accessibilityId).sendKeys(text);
        LOGGER.info(String.format("\n\tTyped [%s] on element with accessibility ID: [%s]\n\t", text, accessibilityId));
    }

    // --- Mobile assertions ---

    @Then("^I see mobile element (\\w+(?: \\w+)*) is displayed by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementDisplayed(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = selectDecision.main(this.by, selectKey, pageElement);
        boolean isDisplayed = mobileDriver.findElement(this.by).isDisplayed();
        Assert.assertTrue(String.format("Expected mobile element [%s] to be displayed", pageKey), isDisplayed);
        LOGGER.info(String.format("\n\tMobile element [%s] is displayed\n\t", pageKey));
    }

    @Then("^I see mobile element (\\w+(?: \\w+)*) text equals \"([^\"]*)\" by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementTextEquals(String pageKey, String expectedText, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = selectDecision.main(this.by, selectKey, pageElement);
        String actualText = mobileDriver.findElement(this.by).getText();
        Assert.assertEquals(String.format("Expected text [%s] but got [%s] for element [%s]",
                expectedText, actualText, pageKey), expectedText, actualText);
        LOGGER.info(String.format("\n\tMobile element [%s] text equals [%s]\n\t", pageKey, expectedText));
    }

    @Then("^I see mobile element (\\w+(?: \\w+)*) text contains \"([^\"]*)\" by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementTextContains(String pageKey, String expectedText, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = selectDecision.main(this.by, selectKey, pageElement);
        String actualText = mobileDriver.findElement(this.by).getText();
        Assert.assertTrue(String.format("Expected text of element [%s] to contain [%s] but got [%s]",
                pageKey, expectedText, actualText), actualText.contains(expectedText));
        LOGGER.info(String.format("\n\tMobile element [%s] text contains [%s]\n\t", pageKey, expectedText));
    }

    @Then("^I see mobile element (\\w+(?: \\w+)*) attribute \"([^\"]*)\" equals \"([^\"]*)\" by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementAttribute(String pageKey, String attribute, String expectedValue, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = selectDecision.main(this.by, selectKey, pageElement);
        String actualValue = mobileDriver.findElement(this.by).getAttribute(attribute);
        Assert.assertEquals(String.format("Expected attribute [%s] of element [%s] to be [%s] but got [%s]",
                attribute, pageKey, expectedValue, actualValue), expectedValue, actualValue);
        LOGGER.info(String.format("\n\tMobile element [%s] attribute [%s] equals [%s]\n\t",
                pageKey, attribute, expectedValue));
    }

    @Then("^I see mobile element with accessibility id \"([^\"]*)\" is displayed$")
    public void iSeeMobileElementByAccessibilityIdDisplayed(String accessibilityId) {
        WebElement element = mobileDriver.findElementByAccessibilityId(accessibilityId);
        Assert.assertTrue(String.format("Expected element with accessibility ID [%s] to be displayed", accessibilityId),
                element.isDisplayed());
        LOGGER.info(String.format("\n\tMobile element with accessibility ID [%s] is displayed\n\t", accessibilityId));
    }

    // --- App install/remove ---

    @Then("^I install app \"([^\"]*)\"$")
    public void iInstallApp(String appPath) {
        mobileDriver.installApp(appPath);
        LOGGER.info(String.format("\n\tInstalled app: [%s]\n\t", appPath));
    }

    @Then("^I remove app \"([^\"]*)\"$")
    public void iRemoveApp(String bundleId) {
        mobileDriver.removeApp(bundleId);
        LOGGER.info(String.format("\n\tRemoved app: [%s]\n\t", bundleId));
    }

    @Then("^I see app \"([^\"]*)\" is installed$")
    public void iSeeAppInstalled(String bundleId) {
        Assert.assertTrue(String.format("Expected app [%s] to be installed", bundleId),
                mobileDriver.isAppInstalled(bundleId));
        LOGGER.info(String.format("\n\tApp [%s] is installed\n\t", bundleId));
    }

    @Then("^I see app \"([^\"]*)\" is not installed$")
    public void iSeeAppNotInstalled(String bundleId) {
        Assert.assertFalse(String.format("Expected app [%s] to NOT be installed", bundleId),
                mobileDriver.isAppInstalled(bundleId));
        LOGGER.info(String.format("\n\tApp [%s] is not installed\n\t", bundleId));
    }

    // --- Orientation assertions ---

    @Then("^I see orientation is (landscape|portrait)$")
    public void iSeeOrientation(String expectedOrientation) {
        ScreenOrientation current = mobileDriver.getOrientation();
        Assert.assertEquals(String.format("Expected orientation [%s] but got [%s]",
                expectedOrientation, current.value()), expectedOrientation.toLowerCase(), current.value().toLowerCase());
        LOGGER.info(String.format("\n\tOrientation is [%s] as expected\n\t", expectedOrientation));
    }

    // --- Android-specific ---

    @Then("^I open notifications$")
    public void iOpenNotifications() {
        if (mobileDriver instanceof AndroidDriver) {
            ((AndroidDriver) mobileDriver).openNotifications();
            LOGGER.info("\n\tNotifications opened\n\t");
        } else {
            LOGGER.info("\n\tOpen notifications is only available on Android\n\t");
        }
    }

    @Then("^I see current activity is \"([^\"]*)\"$")
    public void iSeeCurrentActivity(String expectedActivity) {
        if (mobileDriver instanceof AndroidDriver) {
            String currentActivity = ((AndroidDriver) mobileDriver).currentActivity();
            Assert.assertEquals(String.format("Expected activity [%s] but got [%s]",
                    expectedActivity, currentActivity), expectedActivity, currentActivity);
            LOGGER.info(String.format("\n\tCurrent activity is [%s] as expected\n\t", currentActivity));
        } else {
            LOGGER.info("\n\tCurrent activity is only available on Android\n\t");
        }
    }
}
