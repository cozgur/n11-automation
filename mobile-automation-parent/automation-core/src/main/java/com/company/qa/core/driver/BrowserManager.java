package com.company.qa.core.driver;

import com.company.qa.core.exception.DriverInitializationException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import static com.company.qa.core.util.LogManager.LOGGER;

/**
 * Thread-safe browser driver manager for web automation.
 *
 * <p>Uses Selenium 4 with Selenium Manager, which handles driver binary
 * resolution automatically. Each thread maintains its own isolated
 * {@link WebDriver} instance via {@link ThreadLocal} storage.</p>
 *
 * <p>Supported browsers:</p>
 * <ul>
 *   <li>{@code chrome} - Google Chrome with incognito and maximized window</li>
 *   <li>{@code firefox} - Mozilla Firefox with maximized window</li>
 * </ul>
 */
public class BrowserManager {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    /**
     * Returns the {@link WebDriver} instance bound to the current thread.
     *
     * @return the current thread's driver, or {@code null} if none has been created
     */
    public static WebDriver getDriver() {
        return driverThread.get();
    }

    /**
     * Quits and removes the {@link WebDriver} instance for the current thread.
     *
     * <p>If no driver exists for the current thread, this method is a no-op.</p>
     */
    public static void removeDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            LOGGER.info("Browser driver quit and removed from thread");
        }
        driverThread.remove();
    }

    /**
     * Creates a new browser driver and binds it to the current thread.
     *
     * @param browser the browser type to create (case-insensitive); supported values
     *                are {@code "chrome"} and {@code "firefox"}
     * @return the newly created {@link WebDriver} instance
     * @throws DriverInitializationException if the browser type is unsupported or
     *                                       the driver cannot be created
     */
    public static WebDriver createBrowser(String browser) {
        if (browser == null || browser.trim().isEmpty()) {
            throw new DriverInitializationException("Browser type must not be null or empty");
        }
        WebDriver driver = createBrowserOptions(browser);
        driverThread.set(driver);
        LOGGER.info("Browser [{}] created on thread [{}]", browser, Thread.currentThread().getId());
        return driver;
    }

    /**
     * Creates a {@link WebDriver} instance with browser-specific options.
     *
     * @param browser the browser type (case-insensitive)
     * @return a configured {@link WebDriver}
     * @throws DriverInitializationException if the browser type is not supported
     */
    private static WebDriver createBrowserOptions(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome": {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                options.addArguments("--incognito");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                return new ChromeDriver(options);
            }
            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--start-maximized");
                return new FirefoxDriver(options);
            }
            default:
                throw new DriverInitializationException("Unsupported browser: " + browser);
        }
    }
}
