package com.company.qa.core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import static com.company.qa.core.util.LogManager.LOGGER;

/**
 * Thread-safe browser driver manager.
 * Uses Selenium 4 - Selenium Manager handles driver binaries automatically.
 */
public class BrowserManager {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void removeDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            LOGGER.info("Browser driver quit and removed from thread");
        }
        driverThread.remove();
    }

    public static WebDriver createBrowser(String browser) {
        WebDriver driver = createBrowserOptions(browser);
        driverThread.set(driver);
        LOGGER.info("Browser [{}] created on thread [{}]", browser, Thread.currentThread().getId());
        return driver;
    }

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
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
}
