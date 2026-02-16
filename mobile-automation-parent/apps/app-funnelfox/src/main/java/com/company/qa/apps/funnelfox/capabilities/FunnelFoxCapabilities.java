package com.company.qa.apps.funnelfox.capabilities;

import com.company.qa.core.driver.CapabilityBuilder;
import com.company.qa.core.util.JsonParser;
import com.google.gson.JsonObject;
import io.appium.java_client.remote.options.BaseOptions;

/**
 * Capability factory for the FunnelFox mobile application.
 * Reads device defaults from apps.json, allowing override via system properties.
 */
public class FunnelFoxCapabilities {

    private static final String APP_KEY = "funnelfox";

    /**
     * Creates Android capabilities for FunnelFox.
     * Device and version defaults come from apps.json.
     *
     * @return configured UiAutomator2Options
     */
    public static BaseOptions<?> android() {
        return buildForPlatform("android");
    }

    /**
     * Creates iOS capabilities for FunnelFox.
     * Device and version defaults come from apps.json.
     *
     * @return configured XCUITestOptions
     */
    public static BaseOptions<?> ios() {
        return buildForPlatform("ios");
    }

    /**
     * Creates capabilities for the specified platform.
     *
     * @param platform "android" or "ios"
     * @return configured options
     * @throws IllegalArgumentException if platform is not found in apps.json
     */
    public static BaseOptions<?> forPlatform(String platform) {
        return buildForPlatform(platform.toLowerCase());
    }

    private static BaseOptions<?> buildForPlatform(String platform) {
        JsonObject appsConfig = JsonParser.parse("apps");
        JsonObject appConfig = appsConfig.getAsJsonObject(APP_KEY);
        if (appConfig == null) {
            throw new IllegalArgumentException("App [" + APP_KEY + "] not found in apps.json");
        }
        JsonObject platformConfig = appConfig.getAsJsonObject(platform);
        if (platformConfig == null) {
            throw new IllegalArgumentException("Platform [" + platform + "] not found for app [" + APP_KEY + "]");
        }
        return new CapabilityBuilder()
                .platform(platform)
                .fromJson(platformConfig)
                .build();
    }
}
