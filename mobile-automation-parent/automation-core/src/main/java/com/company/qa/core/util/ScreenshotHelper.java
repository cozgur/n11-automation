package com.company.qa.core.util;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Utility for capturing screenshots.
 */
public class ScreenshotHelper {

    /**
     * Takes a screenshot and returns raw bytes.
     *
     * @param driver the WebDriver instance
     * @return screenshot as byte array, or empty array if driver does not support screenshots
     */
    public static byte[] takeScreenshot(WebDriver driver) {
        if (driver instanceof TakesScreenshot) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        LogManager.LOGGER.warn("Driver does not support screenshots");
        return new byte[0];
    }

    /**
     * Takes a screenshot and attaches it to the Allure report.
     *
     * @param driver the WebDriver instance
     * @return screenshot as byte array
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] allureScreenshot(WebDriver driver) {
        return takeScreenshot(driver);
    }
}
