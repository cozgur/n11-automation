package com.company.qa.core.driver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.options.BaseOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Fluent builder for Appium capability options.
 *
 * <p>Produces {@link UiAutomator2Options} for Android or {@link XCUITestOptions}
 * for iOS based on the configured platform.</p>
 *
 * <p><b>Usage example:</b></p>
 * <pre>{@code
 * BaseOptions<?> options = new CapabilityBuilder()
 *         .platform("android")
 *         .device("Pixel 6")
 *         .version("13.0")
 *         .app("/path/to/app.apk")
 *         .capability("appPackage", "com.example.app")
 *         .build();
 * }</pre>
 *
 * <p>Capabilities can also be loaded from a {@link JsonObject} via {@link #fromJson(JsonObject)},
 * which is typically sourced from a platform section in {@code apps.json}.</p>
 */
public class CapabilityBuilder {

    private String platform;
    private String deviceName;
    private String platformVersion;
    private String app;
    private final Map<String, Object> extras = new HashMap<>();

    /**
     * Sets the target platform.
     *
     * @param platform the platform name ({@code "android"} or {@code "ios"})
     * @return this builder for method chaining
     */
    public CapabilityBuilder platform(String platform) {
        this.platform = platform;
        return this;
    }

    /**
     * Sets the target device or emulator name.
     *
     * @param deviceName the device name (e.g. {@code "Pixel 6"})
     * @return this builder for method chaining
     */
    public CapabilityBuilder device(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    /**
     * Sets the platform OS version.
     *
     * @param platformVersion the version string (e.g. {@code "13.0"})
     * @return this builder for method chaining
     */
    public CapabilityBuilder version(String platformVersion) {
        this.platformVersion = platformVersion;
        return this;
    }

    /**
     * Sets the path or URL to the application binary.
     *
     * @param app the absolute path or URL to the {@code .apk} or {@code .ipa} file
     * @return this builder for method chaining
     */
    public CapabilityBuilder app(String app) {
        this.app = app;
        return this;
    }

    /**
     * Adds a custom capability key-value pair.
     *
     * @param key   the capability name
     * @param value the capability value
     * @return this builder for method chaining
     */
    public CapabilityBuilder capability(String key, Object value) {
        this.extras.put(key, value);
        return this;
    }

    /**
     * Populates builder fields from a JSON object, typically a platform section
     * from {@code apps.json}.
     *
     * <p>Recognized keys ({@code deviceName}, {@code platformVersion}, {@code app})
     * are mapped to their respective builder fields. All other keys are added as
     * extra capabilities. Non-primitive JSON values (objects, arrays) are stored
     * using their {@code toString()} representation.</p>
     *
     * @param platformConfig the JSON configuration object, or {@code null} (no-op)
     * @return this builder for method chaining
     */
    public CapabilityBuilder fromJson(JsonObject platformConfig) {
        if (platformConfig == null) {
            return this;
        }
        for (Map.Entry<String, JsonElement> entry : platformConfig.entrySet()) {
            String key = entry.getKey();
            JsonElement jsonValue = entry.getValue();

            // Safely extract the value: use getAsString for primitives, toString for others
            String value;
            if (jsonValue.isJsonPrimitive()) {
                value = jsonValue.getAsString();
            } else {
                value = jsonValue.toString();
            }

            switch (key) {
                case "deviceName":
                    this.deviceName = value;
                    break;
                case "platformVersion":
                    this.platformVersion = value;
                    break;
                case "app":
                    this.app = value;
                    break;
                default:
                    this.extras.put(key, value);
                    break;
            }
        }
        return this;
    }

    /**
     * Builds the appropriate options object based on the configured platform.
     *
     * @return {@link UiAutomator2Options} for Android, {@link XCUITestOptions} for iOS
     * @throws IllegalArgumentException if the platform has not been set or is unsupported
     */
    public BaseOptions<?> build() {
        if (platform == null || platform.isEmpty()) {
            throw new IllegalArgumentException("Platform must be specified (android|ios)");
        }

        if (platform.equalsIgnoreCase("android")) {
            UiAutomator2Options options = new UiAutomator2Options();
            if (deviceName != null) {
                options.setDeviceName(deviceName);
            }
            if (platformVersion != null) {
                options.setPlatformVersion(platformVersion);
            }
            if (app != null) {
                options.setApp(app);
            }
            for (Map.Entry<String, Object> entry : extras.entrySet()) {
                options.amend(entry.getKey(), entry.getValue());
            }
            return options;

        } else if (platform.equalsIgnoreCase("ios")) {
            XCUITestOptions options = new XCUITestOptions();
            if (deviceName != null) {
                options.setDeviceName(deviceName);
            }
            if (platformVersion != null) {
                options.setPlatformVersion(platformVersion);
            }
            if (app != null) {
                options.setApp(app);
            }
            for (Map.Entry<String, Object> entry : extras.entrySet()) {
                options.amend(entry.getKey(), entry.getValue());
            }
            return options;

        } else {
            throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }
}
