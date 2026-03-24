package com.automation.support;

import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public final class BrowserWindowHelper {

    private BrowserWindowHelper() {
    }

    public static String currentHandle(WebDriver driver) {
        return driver.getWindowHandle();
    }

    public static void switchToWindow(WebDriver driver, String handle) {
        driver.switchTo().window(handle);
    }

    public static List<String> allHandles(WebDriver driver) {
        return new ArrayList<>(driver.getWindowHandles());
    }

    /** Switch to the newest window (e.g. after link opens new tab). */
    public static void switchToLatestWindow(WebDriver driver) {
        List<String> handles = allHandles(driver);
        driver.switchTo().window(handles.get(handles.size() - 1));
    }

    public static void closeCurrentAndSwitchTo(WebDriver driver, String handleToActivate) {
        driver.close();
        driver.switchTo().window(handleToActivate);
    }
}
