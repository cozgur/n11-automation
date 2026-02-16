package com.company.qa.core.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * TestNG retry analyzer that automatically retries failed tests up to a
 * configurable maximum number of times.
 *
 * <p>The maximum retry count can be configured via the {@code -Dretry.max}
 * system property. If not set, it defaults to {@code 2}.</p>
 *
 * <p>Each test method receives its own instance of this analyzer, so the
 * retry counter is isolated per test.</p>
 *
 * @see RetryListener
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int counter = 0;
    private final int maxRetry;

    /**
     * Creates a new retry analyzer, reading the maximum retry count from the
     * {@code retry.max} system property (defaults to {@code 2}).
     */
    public RetryAnalyzer() {
        this.maxRetry = Integer.parseInt(System.getProperty("retry.max", "2"));
    }

    /**
     * Determines whether a failed test should be retried.
     *
     * @param result the result of the test method that just failed
     * @return {@code true} if the retry count has not yet reached the maximum,
     *         {@code false} otherwise
     */
    @Override
    public boolean retry(ITestResult result) {
        if (counter < maxRetry) {
            counter++;
            return true;
        }
        return false;
    }
}
