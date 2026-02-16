package com.company.qa.core.driver;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

/**
 * Resolves a selector type string into a Selenium/Appium {@link By} locator.
 *
 * <p>Supports standard Selenium selectors ({@code id}, {@code name}, {@code xpath},
 * {@code className}, {@code cssSelector}, {@code tagName}, {@code linkText},
 * {@code partialLinkText}) and the Appium-specific {@code accessibilityId}.</p>
 *
 * <p>This is typically used when locator strategies are defined externally
 * (e.g. in JSON element repositories) and need to be converted at runtime.</p>
 */
public class SelectDecision {

    /**
     * Converts a selector type and value pair into a {@link By} locator.
     *
     * @param selectorType the locator strategy name (e.g. {@code "id"},
     *                     {@code "xpath"}, {@code "accessibilityId"})
     * @param value        the locator value (e.g. the XPath expression or element ID)
     * @return the corresponding {@link By} locator instance
     * @throws IllegalArgumentException if the {@code selectorType} is not recognized
     */
    public static By resolve(String selectorType, String value) {
        switch (selectorType) {
            case "id":
                return By.id(value);
            case "name":
                return By.name(value);
            case "xpath":
                return By.xpath(value);
            case "className":
                return By.className(value);
            case "cssSelector":
                return By.cssSelector(value);
            case "tagName":
                return By.tagName(value);
            case "linkText":
                return By.linkText(value);
            case "partialLinkText":
                return By.partialLinkText(value);
            case "accessibilityId":
                return AppiumBy.accessibilityId(value);
            default:
                throw new IllegalArgumentException("Not a valid selector type: " + selectorType);
        }
    }
}
