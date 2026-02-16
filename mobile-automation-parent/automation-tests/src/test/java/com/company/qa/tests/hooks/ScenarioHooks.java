package com.company.qa.tests.hooks;

import com.company.qa.core.context.TestContext;
import com.company.qa.core.driver.BrowserManager;
import com.company.qa.core.driver.MobileDriverManager;
import com.company.qa.core.util.ScreenshotHelper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.util.concurrent.atomic.AtomicInteger;

import static com.company.qa.core.util.LogManager.LOGGER;

public class ScenarioHooks {

    private static final AtomicInteger scenarioCounter = new AtomicInteger(0);
    private static final AtomicInteger failedScenarioCounter = new AtomicInteger(0);

    @Before
    public void beforeScenario(Scenario scenario) {
        int count = scenarioCounter.incrementAndGet();
        LOGGER.info(String.format("\n\n\t[%d] > Scenario [%s] started\t", count, scenario.getName()));
    }

    @After
    public void afterScenario(Scenario scenario) {
        int currentCount = scenarioCounter.get();

        if (scenario.isFailed()) {
            failedScenarioCounter.incrementAndGet();

            // Screenshot on failure
            WebDriver activeDriver = null;
            try {
                activeDriver = MobileDriverManager.getDriver();
            } catch (Exception ignored) {
                // no mobile driver
            }
            if (activeDriver == null) {
                try {
                    activeDriver = BrowserManager.getDriver();
                } catch (Exception ignored) {
                    // no browser driver
                }
            }

            if (activeDriver != null) {
                try {
                    byte[] screenshotBytes = ScreenshotHelper.takeScreenshot(activeDriver);
                    if (screenshotBytes != null && screenshotBytes.length > 0) {
                        scenario.attach(screenshotBytes, "image/png", "failure-screenshot");
                        Allure.addAttachment("failure-screenshot", "image/png",
                                new ByteArrayInputStream(screenshotBytes), ".png");
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to capture screenshot on failure", e);
                }
            }
        }

        String result = scenario.isFailed() ? "with errors" : "successfully";
        LOGGER.info(String.format("\n\t[%d] > Scenario [%s] finished %s\t",
                currentCount, scenario.getName(), result));
        LOGGER.info(String.format("\n\t%d of %d scenarios failed so far\t",
                failedScenarioCounter.get(), currentCount));

        // Cleanup browser driver if it was used
        if (BrowserManager.getDriver() != null) {
            BrowserManager.removeDriver();
        }

        // Cleanup test context
        TestContext.reset();
    }

    @After("@mobile")
    public void afterMobileScenario(Scenario scenario) {
        MobileDriverManager.removeDriver();
    }
}
