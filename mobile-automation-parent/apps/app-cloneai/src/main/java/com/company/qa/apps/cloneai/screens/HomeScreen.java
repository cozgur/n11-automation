package com.company.qa.apps.cloneai.screens;

import com.company.qa.core.driver.MobileDriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HomeScreen {

    private AppiumDriver<MobileElement> driver() {
        return MobileDriverManager.getDriver();
    }

    // Locators
    private static final By SEARCH_FIELD = By.id("com.cloneai.app:id/searchField");
    private static final By PROFILE_BUTTON = By.id("com.cloneai.app:id/profileButton");
    private static final By MENU_BUTTON = By.id("com.cloneai.app:id/menuButton");
    private static final By SETTINGS_BUTTON = By.id("com.cloneai.app:id/settingsButton");

    public WebElement getSearchField() {
        return driver().findElement(SEARCH_FIELD);
    }

    public WebElement getProfileButton() {
        return driver().findElement(PROFILE_BUTTON);
    }

    public WebElement getMenuButton() {
        return driver().findElement(MENU_BUTTON);
    }

    public WebElement getSettingsButton() {
        return driver().findElement(SETTINGS_BUTTON);
    }

    public void search(String query) {
        WebElement field = getSearchField();
        field.clear();
        field.sendKeys(query);
    }

    public void openProfile() {
        getProfileButton().click();
    }

    public void openMenu() {
        getMenuButton().click();
    }

    public void openSettings() {
        getSettingsButton().click();
    }
}
