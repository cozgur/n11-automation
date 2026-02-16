package com.company.qa.core.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * TestNG retry analyzer that retries failed tests up to a configurable maximum.
 * Max retries can be set via -Dretry.max system property (default: 2).
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int counter = 0;
    private final int maxRetry;

    public RetryAnalyzer() {
        this.maxRetry = Integer.parseInt(System.getProperty("retry.max", "2"));
    }

    @Override
    public boolean retry(ITestResult result) {
        if (counter < maxRetry) {
            counter++;
            return true;
        }
        return false;
    }
}
