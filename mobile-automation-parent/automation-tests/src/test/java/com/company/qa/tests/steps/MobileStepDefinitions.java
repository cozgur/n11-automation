package com.company.qa.tests.steps;

import com.google.gson.JsonObject;
import com.company.qa.core.driver.MobileDriverManager;
import com.company.qa.core.driver.SelectDecision;
import com.company.qa.core.action.MobileActions;
import com.company.qa.core.util.JsonParser;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.company.qa.core.util.LogManager.LOGGER;
import static org.assertj.core.api.Assertions.assertThat;

public class MobileStepDefinitions {

    private JsonObject pageObject;
    private By by;

    private AppiumDriver getDriver() {
        return MobileDriverManager.getDriver();
    }

    // --- Driver setup ---

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
        this.pageObject = jsonObject.get(flowKey).getAsJsonObject();
        String urlString = this.pageObject.get("url").getAsString();
        getDriver().get(urlString);
        LOGGER.info(String.format("\n\tMobile navigated to: %s\n\t", urlString));
    }

    // --- App lifecycle ---

    @When("^I launch the mobile app$")
    public void iLaunchApp() {
        getDriver().activateApp(MobileDriverManager.getAppId());
        LOGGER.info("\n\tMobile app launched\n\t");
    }

    @When("^I close the mobile app$")
    public void iCloseApp() {
        getDriver().terminateApp(MobileDriverManager.getAppId());
        LOGGER.info("\n\tMobile app closed\n\t");
    }

    @When("^I reset the mobile app$")
    public void iResetApp() {
        String appId = MobileDriverManager.getAppId();
        getDriver().terminateApp(appId);
        getDriver().activateApp(appId);
        LOGGER.info("\n\tMobile app reset\n\t");
    }

    @When("^I send the mobile app to background for (\\d+) seconds$")
    public void iBackgroundApp(int seconds) {
        getDriver().runAppInBackground(Duration.ofSeconds(seconds));
        LOGGER.info(String.format("\n\tMobile app sent to background for [%d] seconds\n\t", seconds));
    }

    // --- Touch gestures ---

    @Then("^I tap element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iTapElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        WebElement element = getDriver().findElement(this.by);
        new MobileActions(getDriver()).tap(element);
        LOGGER.info(String.format("\n\tTapped element: [%s]\n\t", pageKey));
    }

    @Then("^I long press element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iLongPressElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        WebElement element = getDriver().findElement(this.by);
        new MobileActions(getDriver()).longPress(element);
        LOGGER.info(String.format("\n\tLong pressed element: [%s]\n\t", pageKey));
    }

    @Then("^I tap on coordinates (\\d+) (\\d+)$")
    public void iTapOnCoordinates(int x, int y) {
        new MobileActions(getDriver()).tapCoordinates(x, y);
        LOGGER.info(String.format("\n\tTapped on coordinates: [%d, %d]\n\t", x, y));
    }

    @Then("^I long press on coordinates (\\d+) (\\d+)$")
    public void iLongPressOnCoordinates(int x, int y) {
        new MobileActions(getDriver()).longPressCoordinates(x, y);
        LOGGER.info(String.format("\n\tLong pressed on coordinates: [%d, %d]\n\t", x, y));
    }

    @Then("^I swipe (up|down|left|right)$")
    public void iSwipe(String direction) {
        new MobileActions(getDriver()).swipe(direction);
        LOGGER.info(String.format("\n\tSwiped [%s]\n\t", direction));
    }

    @Then("^I swipe (up|down|left|right) (\\d+) times$")
    public void iSwipeMultiple(String direction, int times) {
        MobileActions actions = new MobileActions(getDriver());
        for (int i = 0; i < times; i++) {
            actions.swipe(direction);
        }
        LOGGER.info(String.format("\n\tSwiped [%s] [%d] times\n\t", direction, times));
    }

    @Then("^I pinch element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iPinchElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        WebElement element = getDriver().findElement(this.by);
        new MobileActions(getDriver()).pinch(element);
        LOGGER.info(String.format("\n\tPinched element: [%s]\n\t", pageKey));
    }

    @Then("^I zoom element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iZoomElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        WebElement element = getDriver().findElement(this.by);
        new MobileActions(getDriver()).zoom(element);
        LOGGER.info(String.format("\n\tZoomed element: [%s]\n\t", pageKey));
    }

    // --- Device actions ---

    @Then("^I hide the keyboard$")
    public void iHideKeyboard() {
        getDriver().hideKeyboard();
        LOGGER.info("\n\tKeyboard hidden\n\t");
    }

    @Then("^I rotate to (landscape|portrait)$")
    public void iRotate(String orientation) {
        if (orientation.equalsIgnoreCase("landscape")) {
            getDriver().rotate(ScreenOrientation.LANDSCAPE);
        } else {
            getDriver().rotate(ScreenOrientation.PORTRAIT);
        }
        LOGGER.info(String.format("\n\tRotated to [%s]\n\t", orientation));
    }

    @Then("^I press the back button$")
    public void iPressBackButton() {
        if (getDriver() instanceof AndroidDriver) {
            ((AndroidDriver) getDriver()).pressKey(new KeyEvent(AndroidKey.BACK));
            LOGGER.info("\n\tPressed back button\n\t");
        } else {
            LOGGER.info("\n\tBack button is only available on Android\n\t");
        }
    }

    @Then("^I press the home button$")
    public void iPressHomeButton() {
        if (getDriver() instanceof AndroidDriver) {
            ((AndroidDriver) getDriver()).pressKey(new KeyEvent(AndroidKey.HOME));
            LOGGER.info("\n\tPressed home button\n\t");
        } else {
            LOGGER.info("\n\tHome button is only available on Android\n\t");
        }
    }

    @Then("^I press the enter key$")
    public void iPressEnterKey() {
        if (getDriver() instanceof AndroidDriver) {
            ((AndroidDriver) getDriver()).pressKey(new KeyEvent(AndroidKey.ENTER));
            LOGGER.info("\n\tPressed enter key\n\t");
        } else {
            LOGGER.info("\n\tAndroid key code is only available on Android\n\t");
        }
    }

    @Then("^I lock the device$")
    public void iLockDevice() {
        if (getDriver() instanceof AndroidDriver) {
            ((AndroidDriver) getDriver()).lockDevice();
        }
        LOGGER.info("\n\tDevice locked\n\t");
    }

    @Then("^I lock the device for (\\d+) seconds$")
    public void iLockDeviceForSeconds(int seconds) {
        if (getDriver() instanceof AndroidDriver) {
            ((AndroidDriver) getDriver()).lockDevice(Duration.ofSeconds(seconds));
        }
        LOGGER.info(String.format("\n\tDevice locked for [%d] seconds\n\t", seconds));
    }

    // --- Context switching ---

    @Then("^I switch to native context$")
    public void iSwitchToNativeContext() {
        getDriver().context("NATIVE_APP");
        LOGGER.info("\n\tSwitched to NATIVE_APP context\n\t");
    }

    @Then("^I switch to webview context$")
    public void iSwitchToWebviewContext() {
        Set<String> contexts = getDriver().getContextHandles();
        for (String context : contexts) {
            if (context.contains("WEBVIEW")) {
                getDriver().context(context);
                LOGGER.info(String.format("\n\tSwitched to context: [%s]\n\t", context));
                return;
            }
        }
        throw new RuntimeException("No WEBVIEW context found. Available contexts: " + contexts);
    }

    @Then("^I switch to context \"([^\"]*)\"$")
    public void iSwitchToContext(String contextName) {
        getDriver().context(contextName);
        LOGGER.info(String.format("\n\tSwitched to context: [%s]\n\t", contextName));
    }

    // --- Mobile element interaction ---

    @Then("^I type \"([^\"]*)\" on mobile element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iTypeOnMobileElement(String text, String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        getDriver().findElement(this.by).sendKeys(text);
        LOGGER.info(String.format("\n\tTyped [%s] on mobile element: [%s]\n\t", text, pageKey));
    }

    @Then("^I clear mobile element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iClearMobileElement(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        getDriver().findElement(this.by).clear();
        LOGGER.info(String.format("\n\tCleared mobile element: [%s]\n\t", pageKey));
    }

    @Then("^I fill mobile by (\\w+(?: \\w+)*)$")
    public void iFillMobileBy(String selectKey, DataTable table) {
        for (List<String> row : table.asLists(String.class)) {
            String key = row.get(0);
            String value = row.get(1);
            JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
            String pageElement = pageElementObject.get(key).getAsString();
            this.by = SelectDecision.resolve(selectKey, pageElement);
            getDriver().findElement(this.by).clear();
            getDriver().findElement(this.by).sendKeys(value);
            LOGGER.info(String.format("\n\tMobile filling key: [%s] with value: [%s]\n\t", key, value));
        }
    }

    @Then("^I tap mobile by (\\w+(?: \\w+)*)$")
    public void iTapMobileBy(String selectKey, DataTable table) {
        for (List<String> row : table.asLists(String.class)) {
            String key = row.get(0);
            JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
            String pageElement = pageElementObject.get(key).getAsString();
            this.by = SelectDecision.resolve(selectKey, pageElement);
            WebElement element = getDriver().findElement(this.by);
            new MobileActions(getDriver()).tap(element);
            LOGGER.info(String.format("\n\tMobile tapped element: [%s]\n\t", key));
        }
    }

    // --- Find by accessibility ID ---

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

    // --- Mobile assertions ---

    @Then("^I see mobile element (\\w+(?: \\w+)*) is displayed by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementDisplayed(String pageKey, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        boolean isDisplayed = getDriver().findElement(this.by).isDisplayed();
        assertThat(isDisplayed)
                .as("Expected mobile element [%s] to be displayed", pageKey)
                .isTrue();
        LOGGER.info(String.format("\n\tMobile element [%s] is displayed\n\t", pageKey));
    }

    @Then("^I see mobile element (\\w+(?: \\w+)*) text equals \"([^\"]*)\" by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementTextEquals(String pageKey, String expectedText, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        String actualText = getDriver().findElement(this.by).getText();
        assertThat(actualText)
                .as("Expected text [%s] but got [%s] for element [%s]",
                        expectedText, actualText, pageKey)
                .isEqualTo(expectedText);
        LOGGER.info(String.format("\n\tMobile element [%s] text equals [%s]\n\t", pageKey, expectedText));
    }

    @Then("^I see mobile element (\\w+(?: \\w+)*) text contains \"([^\"]*)\" by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementTextContains(String pageKey, String expectedText, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        String actualText = getDriver().findElement(this.by).getText();
        assertThat(actualText)
                .as("Expected text of element [%s] to contain [%s] but got [%s]",
                        pageKey, expectedText, actualText)
                .contains(expectedText);
        LOGGER.info(String.format("\n\tMobile element [%s] text contains [%s]\n\t", pageKey, expectedText));
    }

    @Then("^I see mobile element (\\w+(?: \\w+)*) attribute \"([^\"]*)\" equals \"([^\"]*)\" by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementAttribute(String pageKey, String attribute, String expectedValue, String selectKey) {
        JsonObject pageElementObject = this.pageObject.get("elements").getAsJsonObject();
        String pageElement = pageElementObject.get(pageKey).getAsString();
        this.by = SelectDecision.resolve(selectKey, pageElement);
        String actualValue = getDriver().findElement(this.by).getAttribute(attribute);
        assertThat(actualValue)
                .as("Expected attribute [%s] of element [%s] to be [%s] but got [%s]",
                        attribute, pageKey, expectedValue, actualValue)
                .isEqualTo(expectedValue);
        LOGGER.info(String.format("\n\tMobile element [%s] attribute [%s] equals [%s]\n\t",
                pageKey, attribute, expectedValue));
    }

    @Then("^I see mobile element with accessibility id \"([^\"]*)\" is displayed$")
    public void iSeeMobileElementByAccessibilityIdDisplayed(String accessibilityId) {
        WebElement element = getDriver().findElement(AppiumBy.accessibilityId(accessibilityId));
        assertThat(element.isDisplayed())
                .as("Expected element with accessibility ID [%s] to be displayed", accessibilityId)
                .isTrue();
        LOGGER.info(String.format("\n\tMobile element with accessibility ID [%s] is displayed\n\t", accessibilityId));
    }

    // --- App install/remove ---

    @Then("^I install app \"([^\"]*)\"$")
    public void iInstallApp(String appPath) {
        getDriver().installApp(appPath);
        LOGGER.info(String.format("\n\tInstalled app: [%s]\n\t", appPath));
    }

    @Then("^I remove app \"([^\"]*)\"$")
    public void iRemoveApp(String bundleId) {
        getDriver().removeApp(bundleId);
        LOGGER.info(String.format("\n\tRemoved app: [%s]\n\t", bundleId));
    }

    @Then("^I see app \"([^\"]*)\" is installed$")
    public void iSeeAppInstalled(String bundleId) {
        assertThat(getDriver().isAppInstalled(bundleId))
                .as("Expected app [%s] to be installed", bundleId)
                .isTrue();
        LOGGER.info(String.format("\n\tApp [%s] is installed\n\t", bundleId));
    }

    @Then("^I see app \"([^\"]*)\" is not installed$")
    public void iSeeAppNotInstalled(String bundleId) {
        assertThat(getDriver().isAppInstalled(bundleId))
                .as("Expected app [%s] to NOT be installed", bundleId)
                .isFalse();
        LOGGER.info(String.format("\n\tApp [%s] is not installed\n\t", bundleId));
    }

    // --- Orientation assertions ---

    @Then("^I see orientation is (landscape|portrait)$")
    public void iSeeOrientation(String expectedOrientation) {
        ScreenOrientation current = getDriver().getOrientation();
        assertThat(current.value().toLowerCase())
                .as("Expected orientation [%s] but got [%s]",
                        expectedOrientation, current.value())
                .isEqualTo(expectedOrientation.toLowerCase());
        LOGGER.info(String.format("\n\tOrientation is [%s] as expected\n\t", expectedOrientation));
    }

    // --- Android-specific ---

    @Then("^I open notifications$")
    public void iOpenNotifications() {
        if (getDriver() instanceof AndroidDriver) {
            ((AndroidDriver) getDriver()).openNotifications();
            LOGGER.info("\n\tNotifications opened\n\t");
        } else {
            LOGGER.info("\n\tOpen notifications is only available on Android\n\t");
        }
    }

    @Then("^I see current activity is \"([^\"]*)\"$")
    public void iSeeCurrentActivity(String expectedActivity) {
        if (getDriver() instanceof AndroidDriver) {
            String currentActivity = ((AndroidDriver) getDriver()).currentActivity();
            assertThat(currentActivity)
                    .as("Expected activity [%s] but got [%s]",
                            expectedActivity, currentActivity)
                    .isEqualTo(expectedActivity);
            LOGGER.info(String.format("\n\tCurrent activity is [%s] as expected\n\t", currentActivity));
        } else {
            LOGGER.info("\n\tCurrent activity is only available on Android\n\t");
        }
    }
}
