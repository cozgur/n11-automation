package resources.config.driver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import resources.config.utils.jsonParser;

import static resources.logger.LoggerManagement.LOGGER;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class mobileDriverManager {

    private static final String DEFAULT_APPIUM_URL = "http://127.0.0.1:4723/wd/hub";

    private static final ThreadLocal<AppiumDriver<MobileElement>> driverThread = new ThreadLocal<>();

    public static AppiumDriver<MobileElement> getDriver() {
        return driverThread.get();
    }

    public static void removeDriver() {
        AppiumDriver<MobileElement> driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            LOGGER.info("\n\tMobile driver quit and removed from thread\n\t");
        }
        driverThread.remove();
    }

    /**
     * Creates a driver from -D system properties and apps.json config.
     *
     * Required:
     *   -Dplatform=android|ios
     *   -Dapp=cloneai|n11|...        (key in apps.json)
     *
     * Optional overrides:
     *   -DdeviceName=Pixel 6         (overrides apps.json default)
     *   -DplatformVersion=13.0       (overrides apps.json default)
     *   -DappiumUrl=http://...       (overrides default localhost)
     */
    public static AppiumDriver<MobileElement> createDriverFromConfig() {
        String platform = System.getProperty("platform");
        String appName = System.getProperty("app");

        if (platform == null || platform.isEmpty()) {
            throw new IllegalArgumentException("System property -Dplatform is required (android|ios)");
        }
        if (appName == null || appName.isEmpty()) {
            throw new IllegalArgumentException("System property -Dapp is required (e.g. -Dapp=cloneai)");
        }

        JsonObject appsConfig = jsonParser.main("apps");
        JsonObject appConfig = appsConfig.getAsJsonObject(appName);
        if (appConfig == null) {
            throw new IllegalArgumentException("App [" + appName + "] not found in apps.json. " +
                    "Available: " + appsConfig.keySet());
        }

        JsonObject platformConfig = appConfig.getAsJsonObject(platform.toLowerCase());
        if (platformConfig == null) {
            throw new IllegalArgumentException("Platform [" + platform + "] not found for app [" + appName + "] in apps.json. " +
                    "Available: " + appConfig.keySet());
        }

        DesiredCapabilities caps = new DesiredCapabilities();
        for (Map.Entry<String, JsonElement> entry : platformConfig.entrySet()) {
            caps.setCapability(entry.getKey(), entry.getValue().getAsString());
        }

        String deviceNameOverride = System.getProperty("deviceName");
        if (deviceNameOverride != null && !deviceNameOverride.isEmpty()) {
            caps.setCapability("deviceName", deviceNameOverride);
        }

        String versionOverride = System.getProperty("platformVersion");
        if (versionOverride != null && !versionOverride.isEmpty()) {
            caps.setCapability("platformVersion", versionOverride);
        }

        String appPathOverride = System.getProperty("appPath");
        if (appPathOverride != null && !appPathOverride.isEmpty()) {
            caps.setCapability("app", appPathOverride);
        }

        String appiumUrl = System.getProperty("appiumUrl", DEFAULT_APPIUM_URL);

        LOGGER.info(String.format("\n\t[Thread-%d] Creating driver from config: app=[%s] platform=[%s] device=[%s]\n\t",
                Thread.currentThread().getId(), appName, platform, caps.getCapability("deviceName")));

        return initDriver(platform, caps, appiumUrl);
    }

    public static AppiumDriver<MobileElement> createDriver(String platform, String deviceName,
            String platformVersion, String appiumUrl) {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("deviceName", deviceName);
        caps.setCapability("platformVersion", platformVersion);

        return initDriver(platform, caps, appiumUrl);
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

        return initDriver(platform, caps, appiumUrl);
    }

    public static AppiumDriver<MobileElement> createDriverWithApp(String platform, String deviceName,
            String platformVersion, String app) {
        return createDriverWithApp(platform, deviceName, platformVersion, app, DEFAULT_APPIUM_URL);
    }

    public static AppiumDriver<MobileElement> createDriverWithCapabilities(String platform,
            Map<String, String> capabilities, String appiumUrl) {

        DesiredCapabilities caps = new DesiredCapabilities();
        for (Map.Entry<String, String> entry : capabilities.entrySet()) {
            caps.setCapability(entry.getKey(), entry.getValue());
        }

        return initDriver(platform, caps, appiumUrl);
    }

    public static AppiumDriver<MobileElement> createDriverWithCapabilities(String platform,
            Map<String, String> capabilities) {
        return createDriverWithCapabilities(platform, capabilities, DEFAULT_APPIUM_URL);
    }

    private static AppiumDriver<MobileElement> initDriver(String platform, DesiredCapabilities caps,
            String appiumUrl) {

        AppiumDriver<MobileElement> driver;

        try {
            URL url = new URL(appiumUrl);

            if (platform.equalsIgnoreCase("android")) {
                caps.setCapability("platformName", "Android");
                driver = new AndroidDriver<>(url, caps);
                LOGGER.info(String.format("\n\t[Thread-%d] Android driver created for device [%s]\n\t",
                        Thread.currentThread().getId(), caps.getCapability("deviceName")));

            } else if (platform.equalsIgnoreCase("ios")) {
                caps.setCapability("platformName", "iOS");
                caps.setCapability("automationName", "XCUITest");
                driver = new IOSDriver<>(url, caps);
                LOGGER.info(String.format("\n\t[Thread-%d] iOS driver created for device [%s]\n\t",
                        Thread.currentThread().getId(), caps.getCapability("deviceName")));

            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platform);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL: " + appiumUrl, e);
        }

        driverThread.set(driver);
        return driver;
    }
}
