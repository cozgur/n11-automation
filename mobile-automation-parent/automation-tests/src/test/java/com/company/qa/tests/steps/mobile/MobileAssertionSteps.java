package com.company.qa.tests.steps.mobile;

import com.company.qa.core.driver.MobileDriverManager;
import com.company.qa.tests.steps.BaseStepDefinition;
import com.company.qa.tests.steps.ScenarioState;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import static com.company.qa.core.util.LogManager.LOGGER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for mobile assertions
 * (element visibility, text, attributes, app install state, orientation, activity).
 */
public class MobileAssertionSteps extends BaseStepDefinition {

    public MobileAssertionSteps(ScenarioState state) {
        super(state);
    }

    private AppiumDriver getDriver() {
        return MobileDriverManager.getDriver();
    }

    @Then("^I see mobile element (\\w+(?: \\w+)*) is displayed by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementDisplayed(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        boolean isDisplayed = getDriver().findElement(by).isDisplayed();
        assertThat(isDisplayed)
                .as("Expected mobile element [%s] to be displayed", pageKey)
                .isTrue();
        LOGGER.info(String.format("\n\tMobile element [%s] is displayed\n\t", pageKey));
    }

    @Then("^I see mobile element (\\w+(?: \\w+)*) text equals \"([^\"]*)\" by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementTextEquals(String pageKey, String expectedText, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        String actualText = getDriver().findElement(by).getText();
        assertThat(actualText)
                .as("Expected text [%s] but got [%s] for element [%s]",
                        expectedText, actualText, pageKey)
                .isEqualTo(expectedText);
        LOGGER.info(String.format("\n\tMobile element [%s] text equals [%s]\n\t", pageKey, expectedText));
    }

    @Then("^I see mobile element (\\w+(?: \\w+)*) text contains \"([^\"]*)\" by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementTextContains(String pageKey, String expectedText, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        String actualText = getDriver().findElement(by).getText();
        assertThat(actualText)
                .as("Expected text of element [%s] to contain [%s] but got [%s]",
                        pageKey, expectedText, actualText)
                .contains(expectedText);
        LOGGER.info(String.format("\n\tMobile element [%s] text contains [%s]\n\t", pageKey, expectedText));
    }

    @Then("^I see mobile element (\\w+(?: \\w+)*) attribute \"([^\"]*)\" equals \"([^\"]*)\" by (\\w+(?: \\w+)*)$")
    public void iSeeMobileElementAttribute(String pageKey, String attribute, String expectedValue, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        String actualValue = getDriver().findElement(by).getAttribute(attribute);
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

    @Then("^I see orientation is (landscape|portrait)$")
    public void iSeeOrientation(String expectedOrientation) {
        ScreenOrientation current = getDriver().getOrientation();
        assertThat(current.value().toLowerCase())
                .as("Expected orientation [%s] but got [%s]",
                        expectedOrientation, current.value())
                .isEqualTo(expectedOrientation.toLowerCase());
        LOGGER.info(String.format("\n\tOrientation is [%s] as expected\n\t", expectedOrientation));
    }

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
