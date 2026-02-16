package com.company.qa.apps.cloneai.flows;

import com.company.qa.apps.cloneai.screens.HomeScreen;
import com.company.qa.apps.cloneai.screens.SearchResultsScreen;
import com.company.qa.core.base.BaseFlow;

public class SearchFlow extends BaseFlow {

    private final HomeScreen homeScreen;
    private final SearchResultsScreen searchResultsScreen;

    public SearchFlow() {
        this.homeScreen = new HomeScreen();
        this.searchResultsScreen = new SearchResultsScreen();
    }

    public SearchResultsScreen searchFor(String query) {
        homeScreen.search(query);
        return searchResultsScreen;
    }

    public boolean searchAndVerifyResults(String query) {
        searchFor(query);
        return searchResultsScreen.isResultsDisplayed();
    }

    public int searchAndCountResults(String query) {
        searchFor(query);
        return searchResultsScreen.getResultCount();
    }
}
