package com.automation.listeners;

import com.automation.reporting.ExtentManager;
import com.aventstack.extentreports.Status;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ExtentTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ExtentManager.startTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        if (ExtentManager.current() != null) {
            ExtentManager.current().log(Status.PASS, "Passed");
        }
        ExtentManager.endTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (ExtentManager.current() != null && result.getThrowable() != null) {
            ExtentManager.current().log(Status.FAIL, result.getThrowable());
        }
        ExtentManager.endTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (ExtentManager.current() != null) {
            ExtentManager.current().log(Status.SKIP, "Skipped");
        }
        ExtentManager.endTest();
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {
        ExtentManager.flush();
    }
}
