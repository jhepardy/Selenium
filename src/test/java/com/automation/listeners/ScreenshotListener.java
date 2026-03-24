package com.automation.listeners;

import com.automation.config.EnvironmentConfig;
import com.automation.reporting.ExtentManager;
import com.automation.support.ScreenshotHelper;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Path;

public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        EnvironmentConfig env = EnvironmentConfig.load();
        if (!env.isScreenshotOnFailure()) {
            return;
        }
        Object inst = result.getInstance();
        WebDriver driver = extractDriver(inst);
        if (driver == null) {
            return;
        }
        Path shot = ScreenshotHelper.capture(driver, env.getScreenshotDir(), result.getName());
        if (shot == null) {
            return;
        }
        try {
            com.aventstack.extentreports.ExtentTest t = ExtentManager.current();
            if (t != null) {
                t.log(Status.INFO,
                        MediaEntityBuilder.createScreenCaptureFromPath(shot.toAbsolutePath().toString()).build());
            }
        } catch (Exception ignored) {
            // Extent may not be initialized for this thread
        }
    }

    private static WebDriver extractDriver(Object testInstance) {
        if (testInstance == null) {
            return null;
        }
        for (Class<?> c = testInstance.getClass(); c != null; c = c.getSuperclass()) {
            try {
                var f = c.getDeclaredField("driver");
                f.setAccessible(true);
                return (WebDriver) f.get(testInstance);
            } catch (NoSuchFieldException e) {
                // walk hierarchy
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
