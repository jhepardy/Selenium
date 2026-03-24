package com.automation.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class AlertHelper {

    private AlertHelper() {
    }

    public static void accept(WebDriver driver, Duration waitTime) {
        new WebDriverWait(driver, waitTime)
                .until(ExpectedConditions.alertIsPresent())
                .accept();
    }

    public static void dismiss(WebDriver driver, Duration waitTime) {
        new WebDriverWait(driver, waitTime)
                .until(ExpectedConditions.alertIsPresent())
                .dismiss();
    }

    public static String getText(WebDriver driver, Duration waitTime) {
        return new WebDriverWait(driver, waitTime)
                .until(ExpectedConditions.alertIsPresent())
                .getText();
    }

    public static void sendKeys(WebDriver driver, Duration waitTime, String keys) {
        new WebDriverWait(driver, waitTime)
                .until(ExpectedConditions.alertIsPresent())
                .sendKeys(keys);
    }
}
