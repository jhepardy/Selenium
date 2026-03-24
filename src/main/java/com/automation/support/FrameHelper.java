package com.automation.support;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class FrameHelper {

    private FrameHelper() {
    }

    public static void switchToFrame(WebDriver driver, By frameLocator) {
        driver.switchTo().frame(driver.findElement(frameLocator));
    }

    public static void switchToFrameIndex(WebDriver driver, int index) {
        driver.switchTo().frame(index);
    }

    public static void switchToDefault(WebDriver driver) {
        driver.switchTo().defaultContent();
    }
}
