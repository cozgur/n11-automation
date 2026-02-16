package com.company.qa.apps.funnelfox.capabilities;

import com.company.qa.core.driver.CapabilityBuilder;
import io.appium.java_client.remote.options.BaseOptions;

public class FunnelFoxCapabilities {

    public static BaseOptions<?> android() {
        return new CapabilityBuilder()
                .platform("android")
                .capability("appPackage", "com.funnelfox.app")
                .capability("appActivity", ".MainActivity")
                .device("Pixel 6")
                .version("13.0")
                .build();
    }

    public static BaseOptions<?> ios() {
        return new CapabilityBuilder()
                .platform("ios")
                .capability("bundleId", "com.funnelfox.app")
                .device("iPhone 14")
                .version("16.0")
                .build();
    }

    public static BaseOptions<?> forPlatform(String platform) {
        if ("android".equalsIgnoreCase(platform)) {
            return android();
        } else if ("ios".equalsIgnoreCase(platform)) {
            return ios();
        }
        throw new IllegalArgumentException("Unsupported platform: " + platform);
    }
}
