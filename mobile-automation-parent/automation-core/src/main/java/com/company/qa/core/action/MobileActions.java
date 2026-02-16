package com.company.qa.core.action;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

/**
 * Mobile gesture actions using W3C Actions API (PointerInput + Sequence).
 * Compatible with Appium 9 / Selenium 4.
 */
public class MobileActions {

    private final AppiumDriver driver;

    private static final PointerInput FINGER = new PointerInput(PointerInput.Kind.TOUCH, "finger");

    public MobileActions(AppiumDriver driver) {
        this.driver = driver;
    }

    /**
     * Taps on a WebElement using element.click().
     */
    public void tap(WebElement element) {
        element.click();
    }

    /**
     * Taps at specific coordinates using W3C finger sequence.
     */
    public void tapCoordinates(int x, int y) {
        Sequence tapSequence = new Sequence(FINGER, 0);
        tapSequence.addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tapSequence.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tapSequence.addAction(new Pause(FINGER, Duration.ofMillis(50)));
        tapSequence.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(tapSequence));
    }

    /**
     * Long-presses on a WebElement using W3C actions with a pause duration.
     */
    public void longPress(WebElement element) {
        longPress(element, Duration.ofSeconds(2));
    }

    /**
     * Long-presses on a WebElement for the specified duration.
     */
    public void longPress(WebElement element, Duration duration) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.getX() + size.getWidth() / 2;
        int centerY = location.getY() + size.getHeight() / 2;

        Sequence longPressSequence = new Sequence(FINGER, 0);
        longPressSequence.addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
        longPressSequence.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPressSequence.addAction(new Pause(FINGER, duration));
        longPressSequence.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(longPressSequence));
    }

    /**
     * Long-presses at specific coordinates.
     */
    public void longPressCoordinates(int x, int y, Duration duration) {
        Sequence longPressSequence = new Sequence(FINGER, 0);
        longPressSequence.addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        longPressSequence.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        longPressSequence.addAction(new Pause(FINGER, duration));
        longPressSequence.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(longPressSequence));
    }

    /**
     * Swipes in the given direction (up, down, left, right) using W3C finger drag.
     */
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

        performSwipe(startX, startY, endX, endY, Duration.ofMillis(500));
    }

    /**
     * Performs a swipe from one point to another using W3C actions.
     */
    public void performSwipe(int startX, int startY, int endX, int endY, Duration duration) {
        Sequence swipeSequence = new Sequence(FINGER, 0);
        swipeSequence.addAction(FINGER.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipeSequence.addAction(FINGER.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipeSequence.addAction(FINGER.createPointerMove(duration, PointerInput.Origin.viewport(), endX, endY));
        swipeSequence.addAction(FINGER.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(swipeSequence));
    }

    /**
     * Long-presses at specific coordinates with a default 2-second duration.
     */
    public void longPressCoordinates(int x, int y) {
        longPressCoordinates(x, y, Duration.ofSeconds(2));
    }

    /**
     * Pinch gesture on an element using two-finger W3C multi-touch.
     * Moves two fingers from the edges of the element toward its center.
     */
    public void pinch(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.getX() + size.getWidth() / 2;
        int centerY = location.getY() + size.getHeight() / 2;
        int offsetX = size.getWidth() / 4;
        int offsetY = size.getHeight() / 4;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        Sequence seq1 = new Sequence(finger1, 0);
        seq1.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX - offsetX, centerY - offsetY));
        seq1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        seq1.addAction(finger1.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX, centerY));
        seq1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence seq2 = new Sequence(finger2, 0);
        seq2.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX + offsetX, centerY + offsetY));
        seq2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        seq2.addAction(finger2.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX, centerY));
        seq2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(seq1, seq2));
    }

    /**
     * Zoom gesture on an element using two-finger W3C multi-touch.
     * Moves two fingers from the center of the element outward.
     */
    public void zoom(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        int centerX = location.getX() + size.getWidth() / 2;
        int centerY = location.getY() + size.getHeight() / 2;
        int offsetX = size.getWidth() / 4;
        int offsetY = size.getHeight() / 4;

        PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
        PointerInput finger2 = new PointerInput(PointerInput.Kind.TOUCH, "finger2");

        Sequence seq1 = new Sequence(finger1, 0);
        seq1.addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
        seq1.addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        seq1.addAction(finger1.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX - offsetX, centerY - offsetY));
        seq1.addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        Sequence seq2 = new Sequence(finger2, 0);
        seq2.addAction(finger2.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, centerY));
        seq2.addAction(finger2.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        seq2.addAction(finger2.createPointerMove(Duration.ofMillis(500), PointerInput.Origin.viewport(), centerX + offsetX, centerY + offsetY));
        seq2.addAction(finger2.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Arrays.asList(seq1, seq2));
    }
}
