package com.company.qa.core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import static com.company.qa.core.util.LogManager.LOGGER;

import java.io.File;

public class BrowserManager {

    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void removeDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            LOGGER.info("\n\tBrowser driver quit and removed from thread\n\t");
        }
        driverThread.remove();
    }

    public static WebDriver createBrowser(String os, String browser) {
        String osLower = os.toLowerCase();

        if (osLower.contains("win")) {
            if (browser.equals("chrome"))
                System.setProperty("webdriver.chrome.driver", (new File("tools/drivers/chromedriver.exe")).getAbsolutePath());
            else if (browser.contains("firefox"))
                System.setProperty("webdriver.gecko.driver", (new File("tools/drivers/geckodriver.exe")).getAbsolutePath());
            else
                LOGGER.info("\n\n\tBrowser Type Could NOT Been Found !!!");
        } else if (osLower.contains("mac") || osLower.contains("sunos") || osLower.contains("nix") || osLower.contains("nux") || osLower.contains("aix")) {
            if (browser.equals("chrome"))
                System.setProperty("webdriver.chrome.driver", (new File("tools/drivers/chromedriver")).getAbsolutePath());
            else if (browser.contains("firefox"))
                System.setProperty("webdriver.gecko.driver", (new File("tools/drivers/geckodriver")).getAbsolutePath());
            else
                LOGGER.info("\n\n\tBrowser Type Could NOT Been Found !!!");
        } else {
            LOGGER.info("\n\n\tOperating System Could NOT Been Found !!!");
        }

        WebDriver driver = createBrowserOptions(browser);
        driverThread.set(driver);
        return driver;
    }

    private static WebDriver createBrowserOptions(String browser) {
        switch (browser) {
            case "chrome": {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("test-type");
                options.addArguments("start-fullscreen");
                options.addArguments("incognito");
                options.addArguments("no-sandbox");
                return new ChromeDriver(options);
            }
            case "firefox": {
                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("start-fullscreen");
                options.addArguments("test-type");
                return new FirefoxDriver(options);
            }
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }
}
