package com.automation.listeners;

import com.automation.config.EnvironmentConfig;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries failed tests up to {@code retry.max.attempts} from config (each increment = one retry after failure).
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int count;

    @Override
    public boolean retry(ITestResult result) {
        int max = EnvironmentConfig.load().getRetryMaxAttempts();
        if (count < max) {
            count++;
            return true;
        }
        return false;
    }
}
