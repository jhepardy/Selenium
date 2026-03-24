package com.automation.core;

import com.automation.config.EnvironmentConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    private final EnvironmentConfig env;

    protected BasePage(WebDriver driver, EnvironmentConfig env) {
        this.driver = driver;
        this.env = env;
    }

    protected WebDriverWait explicitWait() {
        return new WebDriverWait(driver, Duration.ofSeconds(env.getExplicitWaitSeconds()));
    }

    protected EnvironmentConfig env() {
        return env;
    }
}
