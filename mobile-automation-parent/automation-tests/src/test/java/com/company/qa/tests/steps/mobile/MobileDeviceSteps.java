package com.company.qa.tests.steps.mobile;

import com.company.qa.core.driver.MobileDriverManager;
import com.company.qa.tests.steps.BaseStepDefinition;
import com.company.qa.tests.steps.ScenarioState;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.cucumber.java.en.Then;
import org.openqa.selenium.ScreenOrientation;

import java.time.Duration;
import java.util.Set;

import static com.company.qa.core.util.LogManager.LOGGER;

/**
 * Step definitions for mobile device actions
 * (keyboard, rotation, hardware buttons, lock, context switching).
 */
public class MobileDeviceSteps extends BaseStepDefinition {

    public MobileDeviceSteps(ScenarioState state) {
        super(state);
    }

    private AppiumDriver getDriver() {
        return MobileDriverManager.getDriver();
    }

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
}
