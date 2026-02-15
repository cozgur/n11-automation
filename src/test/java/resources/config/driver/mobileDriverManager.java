package resources.config.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static resources.logger.LoggerManagement.LOGGER;

import java.net.MalformedURLException;
import java.net.URL;

public class mobileDriverManager {

    private static final String DEFAULT_APPIUM_URL = "http://127.0.0.1:4723/wd/hub";

    public static AppiumDriver<MobileElement> createDriver(String platform, String deviceName,
            String platformVersion, String appiumUrl) {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("deviceName", deviceName);
        caps.setCapability("platformVersion", platformVersion);

        try {
            URL url = new URL(appiumUrl);

            if (platform.equalsIgnoreCase("android")) {
                caps.setCapability("platformName", "Android");
                LOGGER.info(String.format("\n\tCreating Android driver for device [%s] version [%s]\n\t",
                        deviceName, platformVersion));
                return new AndroidDriver<>(url, caps);

            } else if (platform.equalsIgnoreCase("ios")) {
                caps.setCapability("platformName", "iOS");
                caps.setCapability("automationName", "XCUITest");
                LOGGER.info(String.format("\n\tCreating iOS driver for device [%s] version [%s]\n\t",
                        deviceName, platformVersion));
                return new IOSDriver<>(url, caps);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL: " + appiumUrl, e);
        }

        throw new IllegalArgumentException("Unsupported platform: " + platform);
    }

    public static AppiumDriver<MobileElement> createDriver(String platform, String deviceName,
            String platformVersion) {
        return createDriver(platform, deviceName, platformVersion, DEFAULT_APPIUM_URL);
    }

    public static AppiumDriver<MobileElement> createDriverWithApp(String platform, String deviceName,
            String platformVersion, String app, String appiumUrl) {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("deviceName", deviceName);
        caps.setCapability("platformVersion", platformVersion);
        caps.setCapability("app", app);

        try {
            URL url = new URL(appiumUrl);

            if (platform.equalsIgnoreCase("android")) {
                caps.setCapability("platformName", "Android");
                LOGGER.info(String.format("\n\tCreating Android driver with app [%s] on device [%s]\n\t",
                        app, deviceName));
                return new AndroidDriver<>(url, caps);

            } else if (platform.equalsIgnoreCase("ios")) {
                caps.setCapability("platformName", "iOS");
                caps.setCapability("automationName", "XCUITest");
                LOGGER.info(String.format("\n\tCreating iOS driver with app [%s] on device [%s]\n\t",
                        app, deviceName));
                return new IOSDriver<>(url, caps);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL: " + appiumUrl, e);
        }

        throw new IllegalArgumentException("Unsupported platform: " + platform);
    }

    public static AppiumDriver<MobileElement> createDriverWithApp(String platform, String deviceName,
            String platformVersion, String app) {
        return createDriverWithApp(platform, deviceName, platformVersion, app, DEFAULT_APPIUM_URL);
    }
}
