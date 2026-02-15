package com.company.qa.core.driver;

import org.openqa.selenium.By;

public class SelectDecision {

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
            default:
                throw new IllegalArgumentException("Not a valid selector type: " + selectorType);
        }
    }
}
