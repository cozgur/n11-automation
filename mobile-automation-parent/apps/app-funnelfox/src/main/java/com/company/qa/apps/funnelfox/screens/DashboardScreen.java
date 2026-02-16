package com.company.qa.apps.funnelfox.screens;

import com.company.qa.core.base.BaseScreen;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class DashboardScreen extends BaseScreen {

    // Locators
    private static final By ANALYTICS_CARD = By.id("com.funnelfox.app:id/analyticsCard");
    private static final By FUNNELS_TAB = By.id("com.funnelfox.app:id/funnelsTab");
    private static final By EVENTS_TAB = By.id("com.funnelfox.app:id/eventsTab");
    private static final By SETTINGS_ICON = By.id("com.funnelfox.app:id/settingsIcon");
    private static final By USER_AVATAR = By.id("com.funnelfox.app:id/userAvatar");

    public WebElement getAnalyticsCard() {
        return find(ANALYTICS_CARD);
    }

    public void openFunnelsTab() {
        tap(FUNNELS_TAB);
    }

    public void openEventsTab() {
        tap(EVENTS_TAB);
    }

    public void openSettings() {
        tap(SETTINGS_ICON);
    }

    public void openUserProfile() {
        tap(USER_AVATAR);
    }
}
