package com.company.qa.core.driver;

import com.google.gson.JsonObject;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.options.BaseOptions;
import com.company.qa.core.config.EnvironmentConfig;
import com.company.qa.core.util.JsonParser;

import static com.company.qa.core.util.LogManager.LOGGER;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class MobileDriverManager {

    private static final ThreadLocal<AppiumDriver> driverThread = new ThreadLocal<>();
    private static final ThreadLocal<String> appIdThread = new ThreadLocal<>();
    private static final ThreadLocal<String> platformThread = new ThreadLocal<>();

    public static AppiumDriver getDriver() {
        return driverThread.get();
    }

    public static String getAppId() {
        return appIdThread.get();
    }

    public static String getPlatform() {
        return platformThread.get();
    }

    public static void removeDriver() {
        AppiumDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            LOGGER.info("Mobile driver quit and removed from thread");
        }
        driverThread.remove();
        appIdThread.remove();
        platformThread.remove();
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
     *   -DappiumUrl=http://...       (overrides default)
     */
    public static AppiumDriver createDriverFromConfig() {
        String platform = System.getProperty("platform");
        String appName = System.getProperty("app");

        if (platform == null || platform.isEmpty()) {
            throw new IllegalArgumentException("System property -Dplatform is required (android|ios)");
        }
        if (appName == null || appName.isEmpty()) {
            throw new IllegalArgumentException("System property -Dapp is required (e.g. -Dapp=cloneai)");
        }

        JsonObject appsConfig = JsonParser.parse("apps");
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

        CapabilityBuilder builder = new CapabilityBuilder()
                .platform(platform)
                .fromJson(platformConfig);

        // System property overrides
        String deviceNameOverride = System.getProperty("deviceName");
        if (deviceNameOverride != null && !deviceNameOverride.isEmpty()) {
            builder.device(deviceNameOverride);
        }

        String versionOverride = System.getProperty("platformVersion");
        if (versionOverride != null && !versionOverride.isEmpty()) {
            builder.version(versionOverride);
        }

        String appPathOverride = System.getProperty("appPath");
        if (appPathOverride != null && !appPathOverride.isEmpty()) {
            builder.app(appPathOverride);
        }

        String appiumUrl = System.getProperty("appiumUrl",
                EnvironmentConfig.getInstance().getAppiumUrl());

        LOGGER.info("[Thread-{}] Creating driver from config: app=[{}] platform=[{}]",
                Thread.currentThread().getId(), appName, platform);

        AppiumDriver driver = initDriver(platform, builder.build(), appiumUrl);

        // Extract and store appId (appPackage for Android, bundleId for iOS)
        String appId = extractAppId(platformConfig, platform);
        appIdThread.set(appId);
        platformThread.set(platform.toLowerCase());

        return driver;
    }

    public static AppiumDriver createDriver(String platform, String deviceName,
            String platformVersion, String appiumUrl) {

        BaseOptions<?> options = new CapabilityBuilder()
                .platform(platform)
                .device(deviceName)
                .version(platformVersion)
                .build();

        AppiumDriver driver = initDriver(platform, options, appiumUrl);
        platformThread.set(platform.toLowerCase());
        return driver;
    }

    public static AppiumDriver createDriver(String platform, String deviceName,
            String platformVersion) {
        return createDriver(platform, deviceName, platformVersion,
                EnvironmentConfig.getInstance().getAppiumUrl());
    }

    public static AppiumDriver createDriverWithApp(String platform, String deviceName,
            String platformVersion, String app, String appiumUrl) {

        BaseOptions<?> options = new CapabilityBuilder()
                .platform(platform)
                .device(deviceName)
                .version(platformVersion)
                .app(app)
                .build();

        AppiumDriver driver = initDriver(platform, options, appiumUrl);
        platformThread.set(platform.toLowerCase());
        return driver;
    }

    public static AppiumDriver createDriverWithApp(String platform, String deviceName,
            String platformVersion, String app) {
        return createDriverWithApp(platform, deviceName, platformVersion, app,
                EnvironmentConfig.getInstance().getAppiumUrl());
    }

    public static AppiumDriver createDriverWithCapabilities(String platform,
            Map<String, String> capabilities, String appiumUrl) {

        CapabilityBuilder builder = new CapabilityBuilder().platform(platform);
        for (Map.Entry<String, String> entry : capabilities.entrySet()) {
            builder.capability(entry.getKey(), entry.getValue());
        }

        AppiumDriver driver = initDriver(platform, builder.build(), appiumUrl);
        platformThread.set(platform.toLowerCase());
        return driver;
    }

    public static AppiumDriver createDriverWithCapabilities(String platform,
            Map<String, String> capabilities) {
        return createDriverWithCapabilities(platform, capabilities,
                EnvironmentConfig.getInstance().getAppiumUrl());
    }

    private static AppiumDriver initDriver(String platform, BaseOptions<?> options, String appiumUrl) {
        AppiumDriver driver;

        try {
            URL url = new URL(appiumUrl);

            if (platform.equalsIgnoreCase("android")) {
                driver = new AndroidDriver(url, options);
                LOGGER.info("[Thread-{}] Android driver created", Thread.currentThread().getId());

            } else if (platform.equalsIgnoreCase("ios")) {
                driver = new IOSDriver(url, options);
                LOGGER.info("[Thread-{}] iOS driver created", Thread.currentThread().getId());

            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platform);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL: " + appiumUrl, e);
        }

        driverThread.set(driver);
        return driver;
    }

    private static String extractAppId(JsonObject platformConfig, String platform) {
        if (platform.equalsIgnoreCase("android")) {
            if (platformConfig.has("appPackage")) {
                return platformConfig.get("appPackage").getAsString();
            }
        } else if (platform.equalsIgnoreCase("ios")) {
            if (platformConfig.has("bundleId")) {
                return platformConfig.get("bundleId").getAsString();
            }
        }
        return null;
    }
}
