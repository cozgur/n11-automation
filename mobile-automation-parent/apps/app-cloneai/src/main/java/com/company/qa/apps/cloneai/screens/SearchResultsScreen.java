package com.company.qa.apps.cloneai.screens;

import com.company.qa.core.base.BaseScreen;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchResultsScreen extends BaseScreen {

    private static final By RESULTS_LIST = By.id("com.cloneai.app:id/searchResults");
    private static final By RESULT_ITEM = By.id("com.cloneai.app:id/resultItem");
    private static final By NO_RESULTS_LABEL = By.id("com.cloneai.app:id/noResultsLabel");

    public boolean isResultsDisplayed() {
        return find(RESULTS_LIST).isDisplayed();
    }

    public List<WebElement> getResultItems() {
        return findAll(RESULT_ITEM);
    }

    public int getResultCount() {
        return getResultItems().size();
    }

    public void tapResult(int index) {
        getResultItems().get(index).click();
    }

    public boolean isNoResultsDisplayed() {
        List<WebElement> elements = findAll(NO_RESULTS_LABEL);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }
}
