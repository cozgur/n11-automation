package com.company.qa.apps.funnelfox.flows;

import com.company.qa.apps.funnelfox.screens.DashboardScreen;
import com.company.qa.core.base.BaseFlow;

public class DashboardFlow extends BaseFlow {

    private final DashboardScreen dashboardScreen;

    public DashboardFlow() {
        this.dashboardScreen = new DashboardScreen();
    }

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
