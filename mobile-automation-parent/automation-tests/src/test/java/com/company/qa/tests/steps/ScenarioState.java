package com.company.qa.tests.steps;

import com.google.gson.JsonObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Shared scenario state injected by PicoContainer into step definition classes.
 * Holds the current page object, resolved locator, and web driver between steps.
 */
public class ScenarioState {
    private JsonObject pageObject;
    private By currentLocator;
    private WebDriver webDriver;

    public JsonObject getPageObject() { return pageObject; }
    public void setPageObject(JsonObject pageObject) { this.pageObject = pageObject; }
    public By getCurrentLocator() { return currentLocator; }
    public void setCurrentLocator(By currentLocator) { this.currentLocator = currentLocator; }
    public WebDriver getWebDriver() { return webDriver; }
    public void setWebDriver(WebDriver webDriver) { this.webDriver = webDriver; }
}
