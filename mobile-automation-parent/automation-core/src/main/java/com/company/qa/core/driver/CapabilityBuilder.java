package com.company.qa.core.driver;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.options.BaseOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Fluent builder for Appium 9 capability options.
 * Produces UiAutomator2Options for Android or XCUITestOptions for iOS.
 */
public class CapabilityBuilder {

    private String platform;
    private String deviceName;
    private String platformVersion;
    private String app;
    private final Map<String, Object> extras = new HashMap<>();

    public CapabilityBuilder platform(String platform) {
        this.platform = platform;
        return this;
    }

    public CapabilityBuilder device(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public CapabilityBuilder version(String platformVersion) {
        this.platformVersion = platformVersion;
        return this;
    }

    public CapabilityBuilder app(String app) {
        this.app = app;
        return this;
    }

    public CapabilityBuilder capability(String key, Object value) {
        this.extras.put(key, value);
        return this;
    }

    /**
     * Populate builder fields from a JSON object (typically a platform section from apps.json).
     */
    public CapabilityBuilder fromJson(JsonObject platformConfig) {
        if (platformConfig == null) {
            return this;
        }
        for (Map.Entry<String, JsonElement> entry : platformConfig.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();

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
     * Builds the appropriate options object based on platform.
     *
     * @return UiAutomator2Options for android, XCUITestOptions for ios
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
