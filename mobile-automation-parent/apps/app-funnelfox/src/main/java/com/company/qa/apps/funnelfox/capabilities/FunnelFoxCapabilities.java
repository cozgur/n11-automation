package com.company.qa.apps.funnelfox.capabilities;

import org.openqa.selenium.remote.DesiredCapabilities;

public class FunnelFoxCapabilities {

    public static DesiredCapabilities android() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appPackage", "com.funnelfox.app");
        caps.setCapability("appActivity", ".MainActivity");
        caps.setCapability("deviceName", "Pixel 6");
        caps.setCapability("platformVersion", "13.0");
        return caps;
    }

    public static DesiredCapabilities ios() {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "iOS");
        caps.setCapability("automationName", "XCUITest");
        caps.setCapability("bundleId", "com.funnelfox.app");
        caps.setCapability("deviceName", "iPhone 14");
        caps.setCapability("platformVersion", "16.0");
        return caps;
    }

    public static DesiredCapabilities forPlatform(String platform) {
        if ("android".equalsIgnoreCase(platform)) {
            return android();
        } else if ("ios".equalsIgnoreCase(platform)) {
            return ios();
        }
        throw new IllegalArgumentException("Unsupported platform: " + platform);
    }
}
