package com.company.qa.apps.funnelfox.screens;

import com.company.qa.core.driver.MobileDriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DashboardScreen {

    private AppiumDriver<MobileElement> driver() {
        return MobileDriverManager.getDriver();
    }

    // Locators
    private static final By ANALYTICS_CARD = By.id("com.funnelfox.app:id/analyticsCard");
    private static final By FUNNELS_TAB = By.id("com.funnelfox.app:id/funnelsTab");
    private static final By EVENTS_TAB = By.id("com.funnelfox.app:id/eventsTab");
    private static final By SETTINGS_ICON = By.id("com.funnelfox.app:id/settingsIcon");
    private static final By USER_AVATAR = By.id("com.funnelfox.app:id/userAvatar");

    public WebElement getAnalyticsCard() {
        return driver().findElement(ANALYTICS_CARD);
    }

    public void openFunnelsTab() {
        driver().findElement(FUNNELS_TAB).click();
    }

    public void openEventsTab() {
        driver().findElement(EVENTS_TAB).click();
    }

    public void openSettings() {
        driver().findElement(SETTINGS_ICON).click();
    }

    public void openUserProfile() {
        driver().findElement(USER_AVATAR).click();
    }
}
