package com.automation.support;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public final class ScreenshotHelper {

    private static final Logger log = LogManager.getLogger(ScreenshotHelper.class);

    private ScreenshotHelper() {
    }

    public static Path capture(WebDriver driver, String directory, String baseName) {
        if (!(driver instanceof TakesScreenshot ts)) {
            log.warn("Driver does not support screenshots");
            return null;
        }
        byte[] png = ts.getScreenshotAs(OutputType.BYTES);
        Path dir = Path.of(directory);
        try {
            Files.createDirectories(dir);
            String safe = baseName.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path file = dir.resolve(safe + "_" + Instant.now().toEpochMilli() + ".png");
            Files.write(file, png);
            log.info("Screenshot saved: {}", file.toAbsolutePath());
            return file;
        } catch (IOException e) {
            log.error("Failed to save screenshot", e);
            return null;
        }
    }
}
