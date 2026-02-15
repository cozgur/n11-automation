package com.company.qa.apps.cloneai.flows;

import com.company.qa.apps.cloneai.screens.HomeScreen;
import com.company.qa.apps.cloneai.screens.SearchResultsScreen;

public class SearchFlow {

    private final HomeScreen homeScreen = new HomeScreen();
    private final SearchResultsScreen searchResultsScreen = new SearchResultsScreen();

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
