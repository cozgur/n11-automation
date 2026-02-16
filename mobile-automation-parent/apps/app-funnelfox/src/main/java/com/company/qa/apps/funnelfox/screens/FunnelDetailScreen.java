package com.company.qa.apps.funnelfox.screens;

import com.company.qa.core.base.BaseScreen;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class FunnelDetailScreen extends BaseScreen {

    private static final By FUNNEL_NAME = By.id("com.funnelfox.app:id/funnelName");
    private static final By STEP_LIST = By.id("com.funnelfox.app:id/stepList");
    private static final By STEP_ITEM = By.id("com.funnelfox.app:id/stepItem");
    private static final By CONVERSION_RATE = By.id("com.funnelfox.app:id/conversionRate");
    private static final By BACK_BUTTON = By.id("com.funnelfox.app:id/backButton");

    public String getFunnelName() {
        return find(FUNNEL_NAME).getText();
    }

    public List<WebElement> getSteps() {
        return findAll(STEP_ITEM);
    }

    public String getConversionRate() {
        return find(CONVERSION_RATE).getText();
    }

    public void goBack() {
        tap(BACK_BUTTON);
    }
}
