package com.company.qa.tests.steps;

import com.google.gson.JsonObject;
import com.company.qa.core.driver.SelectDecision;
import org.openqa.selenium.By;

/**
 * Base class for all step definitions. Provides shared helper methods
 * to eliminate code duplication across step definition classes.
 */
public abstract class BaseStepDefinition {

    protected final ScenarioState state;

    protected BaseStepDefinition(ScenarioState state) {
        this.state = state;
    }

    /**
     * Resolves a page element locator from the current page object.
     * Reads the element key from pages.json and creates a By locator.
     *
     * @param pageKey   element key in the page object's "elements" section
     * @param selectKey selector strategy (id, xpath, cssSelector, accessibilityId, etc.)
     * @return resolved By locator
     * @throws IllegalArgumentException if pageKey is not found in page elements
     */
    protected By resolveElement(String pageKey, String selectKey) {
        JsonObject elements = state.getPageObject().get("elements").getAsJsonObject();
        if (!elements.has(pageKey)) {
            throw new IllegalArgumentException("Element [" + pageKey + "] not found in page object");
        }
        String locatorValue = elements.get(pageKey).getAsString();
        By by = SelectDecision.resolve(selectKey, locatorValue);
        state.setCurrentLocator(by);
        return by;
    }
}
