package com.company.qa.apps.cloneai.screens;

import com.company.qa.core.base.BaseScreen;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class HomeScreen extends BaseScreen {

    // Locators
    private static final By SEARCH_FIELD = By.id("com.cloneai.app:id/searchField");
    private static final By PROFILE_BUTTON = By.id("com.cloneai.app:id/profileButton");
    private static final By MENU_BUTTON = By.id("com.cloneai.app:id/menuButton");
    private static final By SETTINGS_BUTTON = By.id("com.cloneai.app:id/settingsButton");

    public WebElement getSearchField() {
        return find(SEARCH_FIELD);
    }

    public WebElement getProfileButton() {
        return find(PROFILE_BUTTON);
    }

    public WebElement getMenuButton() {
        return find(MENU_BUTTON);
    }

    public WebElement getSettingsButton() {
        return find(SETTINGS_BUTTON);
    }

    public void search(String query) {
        type(SEARCH_FIELD, query);
    }

    public void openProfile() {
        tap(PROFILE_BUTTON);
    }

    public void openMenu() {
        tap(MENU_BUTTON);
    }

    public void openSettings() {
        tap(SETTINGS_BUTTON);
    }
}
