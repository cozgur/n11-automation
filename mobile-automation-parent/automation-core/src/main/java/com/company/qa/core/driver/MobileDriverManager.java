package com.company.qa.core.driver;

import com.company.qa.core.exception.DriverInitializationException;
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

/**
 * Thread-safe mobile driver manager for Appium-based automation.
 *
 * <p>Manages the lifecycle of {@link AppiumDriver} instances using {@link ThreadLocal}
 * storage, ensuring each test thread operates with its own isolated driver. Supports
 * both Android ({@link AndroidDriver}) and iOS ({@link IOSDriver}) platforms.</p>
 *
 * <p>Driver creation can be configured via:</p>
 * <ul>
 *   <li>System properties ({@code -Dplatform}, {@code -Dapp}, etc.) combined with
 *       {@code apps.json} configuration</li>
 *   <li>Explicit parameters passed to the various {@code createDriver} methods</li>
 *   <li>Custom capability maps for advanced scenarios</li>
 * </ul>
 */
public class MobileDriverManager {

    private static final ThreadLocal<AppiumDriver> driverThread = new ThreadLocal<>();
    private static final ThreadLocal<String> appIdThread = new ThreadLocal<>();
    private static final ThreadLocal<String> platformThread = new ThreadLocal<>();

    /**
     * Returns the {@link AppiumDriver} instance bound to the current thread.
     *
     * @return the current thread's driver, or {@code null} if none has been created
     */
    public static AppiumDriver getDriver() {
        return driverThread.get();
    }

    /**
     * Returns the application identifier for the current thread's app under test.
     *
     * @return the app package (Android) or bundle ID (iOS), or {@code null} if not set
     */
    public static String getAppId() {
        return appIdThread.get();
    }

    /**
     * Returns the platform name for the current thread's driver.
     *
     * @return the lowercase platform name (e.g. {@code "android"} or {@code "ios"}),
     *         or {@code null} if not set
     */
    public static String getPlatform() {
        return platformThread.get();
    }

    /**
     * Quits and removes the {@link AppiumDriver} and associated metadata for the
     * current thread.
     *
     * <p>If no driver exists for the current thread, this method is a no-op
     * (aside from clearing the thread-local references).</p>
     */
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
     * Creates a driver from {@code -D} system properties and {@code apps.json} config.
     *
     * <p><b>Required system properties:</b></p>
     * <ul>
     *   <li>{@code -Dplatform=android|ios}</li>
     *   <li>{@code -Dapp=cloneai|n11|...} (key in {@code apps.json})</li>
     * </ul>
     *
     * <p><b>Optional system property overrides:</b></p>
     * <ul>
     *   <li>{@code -DdeviceName=Pixel 6} - overrides the device name from {@code apps.json}</li>
     *   <li>{@code -DplatformVersion=13.0} - overrides the platform version</li>
     *   <li>{@code -DappPath=/path/to/app} - overrides the app path</li>
     *   <li>{@code -DappiumUrl=http://...} - overrides the default Appium server URL</li>
     * </ul>
     *
     * @return the newly created {@link AppiumDriver} bound to the current thread
     * @throws IllegalArgumentException      if required system properties are missing or
     *                                       the app/platform is not found in {@code apps.json}
     * @throws DriverInitializationException if the driver cannot be created
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
        if (appsConfig == null) {
            throw new DriverInitializationException("Failed to load apps.json configuration");
        }

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

    /**
     * Creates a driver with the specified platform, device, version, and Appium server URL.
     *
     * @param platform        the target platform ({@code "android"} or {@code "ios"})
     * @param deviceName      the device or emulator name
     * @param platformVersion the platform OS version
     * @param appiumUrl       the Appium server URL
     * @return the newly created {@link AppiumDriver} bound to the current thread
     * @throws DriverInitializationException if the driver cannot be created
     */
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

    /**
     * Creates a driver using the default Appium server URL from {@link EnvironmentConfig}.
     *
     * @param platform        the target platform ({@code "android"} or {@code "ios"})
     * @param deviceName      the device or emulator name
     * @param platformVersion the platform OS version
     * @return the newly created {@link AppiumDriver} bound to the current thread
     * @throws DriverInitializationException if the driver cannot be created
     */
    public static AppiumDriver createDriver(String platform, String deviceName,
            String platformVersion) {
        return createDriver(platform, deviceName, platformVersion,
                EnvironmentConfig.getInstance().getAppiumUrl());
    }

    /**
     * Creates a driver with an application path and the specified Appium server URL.
     *
     * @param platform        the target platform ({@code "android"} or {@code "ios"})
     * @param deviceName      the device or emulator name
     * @param platformVersion the platform OS version
     * @param app             the absolute path or URL to the application binary
     * @param appiumUrl       the Appium server URL
     * @return the newly created {@link AppiumDriver} bound to the current thread
     * @throws DriverInitializationException if the driver cannot be created
     */
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

    /**
     * Creates a driver with an application path using the default Appium server URL.
     *
     * @param platform        the target platform ({@code "android"} or {@code "ios"})
     * @param deviceName      the device or emulator name
     * @param platformVersion the platform OS version
     * @param app             the absolute path or URL to the application binary
     * @return the newly created {@link AppiumDriver} bound to the current thread
     * @throws DriverInitializationException if the driver cannot be created
     */
    public static AppiumDriver createDriverWithApp(String platform, String deviceName,
            String platformVersion, String app) {
        return createDriverWithApp(platform, deviceName, platformVersion, app,
                EnvironmentConfig.getInstance().getAppiumUrl());
    }

    /**
     * Creates a driver with a custom capability map and the specified Appium server URL.
     *
     * @param platform     the target platform ({@code "android"} or {@code "ios"})
     * @param capabilities a map of capability key-value pairs
     * @param appiumUrl    the Appium server URL
     * @return the newly created {@link AppiumDriver} bound to the current thread
     * @throws DriverInitializationException if the driver cannot be created
     */
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

    /**
     * Creates a driver with a custom capability map using the default Appium server URL.
     *
     * @param platform     the target platform ({@code "android"} or {@code "ios"})
     * @param capabilities a map of capability key-value pairs
     * @return the newly created {@link AppiumDriver} bound to the current thread
     * @throws DriverInitializationException if the driver cannot be created
     */
    public static AppiumDriver createDriverWithCapabilities(String platform,
            Map<String, String> capabilities) {
        return createDriverWithCapabilities(platform, capabilities,
                EnvironmentConfig.getInstance().getAppiumUrl());
    }

    /**
     * Initializes the platform-specific driver and binds it to the current thread.
     *
     * @param platform  the target platform
     * @param options   the configured capability options
     * @param appiumUrl the Appium server URL
     * @return the created {@link AppiumDriver}
     * @throws DriverInitializationException if the URL is malformed or the platform
     *                                       is unsupported
     */
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
                throw new DriverInitializationException("Unsupported platform: " + platform);
            }
        } catch (MalformedURLException e) {
            throw new DriverInitializationException("Invalid Appium server URL: " + appiumUrl, e);
        }

        driverThread.set(driver);
        return driver;
    }

    /**
     * Extracts the application identifier from a platform configuration block.
     *
     * @param platformConfig the JSON object containing platform-specific settings
     * @param platform       the platform name ({@code "android"} or {@code "ios"})
     * @return the app package (Android) or bundle ID (iOS), or {@code null} if not
     *         present in the configuration
     */
    private static String extractAppId(JsonObject platformConfig, String platform) {
        if (platformConfig == null) {
            return null;
        }
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
