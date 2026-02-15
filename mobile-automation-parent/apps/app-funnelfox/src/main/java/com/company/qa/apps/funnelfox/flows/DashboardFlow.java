package com.company.qa.apps.funnelfox.flows;

import com.company.qa.apps.funnelfox.screens.DashboardScreen;

public class DashboardFlow {

    private final DashboardScreen dashboardScreen = new DashboardScreen();

    public void navigateToFunnels() {
        dashboardScreen.openFunnelsTab();
    }

    public void navigateToEvents() {
        dashboardScreen.openEventsTab();
    }

    public void navigateToSettings() {
        dashboardScreen.openSettings();
    }
}
