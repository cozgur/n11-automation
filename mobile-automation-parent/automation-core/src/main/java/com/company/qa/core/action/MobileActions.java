package com.company.qa.core.action;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;

public class MobileActions {

    private final AppiumDriver<MobileElement> driver;

    public MobileActions(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
    }

    public void tap(WebElement element) {
        new TouchAction(driver).tap(element).perform();
    }

    public void longPress(WebElement element) {
        new TouchAction(driver).longPress(element).release().perform();
    }

    public void tapCoordinates(int x, int y) {
        new TouchAction(driver).tap(x, y).perform();
    }

    public void longPressCoordinates(int x, int y) {
        new TouchAction(driver).longPress(x, y).release().perform();
    }

    public void swipe(String direction) {
        Dimension size = driver.manage().window().getSize();
        int centerX = size.width / 2;
        int centerY = size.height / 2;

        int startX, startY, endX, endY;

        switch (direction.toLowerCase()) {
            case "up":
                startX = centerX;
                startY = (int) (size.height * 0.7);
                endX = centerX;
                endY = (int) (size.height * 0.3);
                break;
            case "down":
                startX = centerX;
                startY = (int) (size.height * 0.3);
                endX = centerX;
                endY = (int) (size.height * 0.7);
                break;
            case "left":
                startX = (int) (size.width * 0.8);
                startY = centerY;
                endX = (int) (size.width * 0.2);
                endY = centerY;
                break;
            case "right":
                startX = (int) (size.width * 0.2);
                startY = centerY;
                endX = (int) (size.width * 0.8);
                endY = centerY;
                break;
            default:
                throw new IllegalArgumentException("Invalid swipe direction: " + direction);
        }

        driver.swipe(startX, startY, endX, endY, 500);
    }

    public void pinch(WebElement element) {
        driver.pinch(element);
    }

    public void zoom(WebElement element) {
        driver.zoom(element);
    }
}
