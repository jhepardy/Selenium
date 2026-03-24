package com.automation.support;

import com.automation.config.EnvironmentConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class ModalHelper {

    private ModalHelper() {
    }

    public static void waitForModalVisible(WebDriver driver, EnvironmentConfig env, By modalRoot) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(env.getExplicitWaitSeconds()));
        w.until(ExpectedConditions.visibilityOfElementLocated(modalRoot));
    }

    public static void waitForModalHidden(WebDriver driver, EnvironmentConfig env, By modalRoot) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(env.getExplicitWaitSeconds()));
        w.until(ExpectedConditions.invisibilityOfElementLocated(modalRoot));
    }
}
