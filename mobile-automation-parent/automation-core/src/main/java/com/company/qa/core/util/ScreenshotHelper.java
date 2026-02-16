package com.company.qa.core.util;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Utility class for capturing screenshots from a {@link WebDriver} session.
 *
 * <p>Supports both raw byte-array capture and automatic attachment to
 * Allure reports via the {@link Attachment} annotation.</p>
 */
public class ScreenshotHelper {

    /**
     * Takes a screenshot and returns the raw PNG bytes.
     *
     * <p>If the driver does not implement {@link TakesScreenshot}, a warning
     * is logged and an empty byte array is returned.</p>
     *
     * @param driver the WebDriver instance to capture the screenshot from
     * @return the screenshot as a PNG byte array, or an empty array if the
     *         driver does not support screenshots
     */
    public static byte[] takeScreenshot(WebDriver driver) {
        if (driver instanceof TakesScreenshot) {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        }
        LogManager.LOGGER.warn("Driver does not support screenshots");
        return new byte[0];
    }

    /**
     * Takes a screenshot and attaches it to the Allure report as a PNG image.
     *
     * @param driver the WebDriver instance to capture the screenshot from
     * @return the screenshot as a PNG byte array
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] allureScreenshot(WebDriver driver) {
        return takeScreenshot(driver);
    }
}
