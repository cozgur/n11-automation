package com.company.qa.apps.cloneai.screens;

import com.company.qa.core.driver.MobileDriverManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchResultsScreen {

    private AppiumDriver<MobileElement> driver() {
        return MobileDriverManager.getDriver();
    }

    private static final By RESULTS_LIST = By.id("com.cloneai.app:id/searchResults");
    private static final By RESULT_ITEM = By.id("com.cloneai.app:id/resultItem");
    private static final By NO_RESULTS_LABEL = By.id("com.cloneai.app:id/noResultsLabel");

    public boolean isResultsDisplayed() {
        return driver().findElement(RESULTS_LIST).isDisplayed();
    }

    public List<WebElement> getResultItems() {
        return driver().findElements(RESULT_ITEM);
    }

    public int getResultCount() {
        return getResultItems().size();
    }

    public void tapResult(int index) {
        getResultItems().get(index).click();
    }

    public boolean isNoResultsDisplayed() {
        return !driver().findElements(NO_RESULTS_LABEL).isEmpty()
                && driver().findElement(NO_RESULTS_LABEL).isDisplayed();
    }
}
