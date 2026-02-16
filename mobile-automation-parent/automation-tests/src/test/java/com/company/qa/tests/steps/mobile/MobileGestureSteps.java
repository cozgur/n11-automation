package com.company.qa.tests.steps.mobile;

import com.company.qa.core.driver.MobileDriverManager;
import com.company.qa.core.action.MobileActions;
import com.company.qa.tests.steps.BaseStepDefinition;
import com.company.qa.tests.steps.ScenarioState;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.Then;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static com.company.qa.core.util.LogManager.LOGGER;

/**
 * Step definitions for mobile touch gestures
 * (tap, long press, swipe, pinch, zoom).
 */
public class MobileGestureSteps extends BaseStepDefinition {

    public MobileGestureSteps(ScenarioState state) {
        super(state);
    }

    private AppiumDriver getDriver() {
        return MobileDriverManager.getDriver();
    }

    @Then("^I tap element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iTapElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        WebElement element = getDriver().findElement(by);
        new MobileActions(getDriver()).tap(element);
        LOGGER.info(String.format("\n\tTapped element: [%s]\n\t", pageKey));
    }

    @Then("^I long press element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iLongPressElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        WebElement element = getDriver().findElement(by);
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
        By by = resolveElement(pageKey, selectKey);
        WebElement element = getDriver().findElement(by);
        new MobileActions(getDriver()).pinch(element);
        LOGGER.info(String.format("\n\tPinched element: [%s]\n\t", pageKey));
    }

    @Then("^I zoom element (\\w+(?: \\w+)*) by (\\w+(?: \\w+)*)$")
    public void iZoomElement(String pageKey, String selectKey) {
        By by = resolveElement(pageKey, selectKey);
        WebElement element = getDriver().findElement(by);
        new MobileActions(getDriver()).zoom(element);
        LOGGER.info(String.format("\n\tZoomed element: [%s]\n\t", pageKey));
    }
}
