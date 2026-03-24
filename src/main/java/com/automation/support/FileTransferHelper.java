package com.automation.support;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.nio.file.Path;

public final class FileTransferHelper {

    private FileTransferHelper() {
    }

    /**
     * Upload via hidden or visible {@code <input type="file">}. Pass absolute path.
     */
    public static void uploadFile(WebElement fileInput, Path absoluteFile) {
        fileInput.sendKeys(absoluteFile.toAbsolutePath().toString());
    }

    public static void uploadFileByLocator(org.openqa.selenium.WebDriver driver, By fileInput, Path absoluteFile) {
        WebElement el = driver.findElement(fileInput);
        el.sendKeys(absoluteFile.toAbsolutePath().toString());
    }
}
