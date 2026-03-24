package com.automation.tests.base;

import com.automation.config.EnvironmentConfig;
import com.automation.driver.DriverFactory;
import com.automation.listeners.ExtentTestListener;
import com.automation.listeners.ScreenshotListener;
import io.restassured.RestAssured;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners({ScreenshotListener.class, ExtentTestListener.class})
public abstract class AbstractWebTest {

    protected WebDriver driver;
    protected EnvironmentConfig env;

    @BeforeClass(alwaysRun = true)
    public void loadEnvironment() {
        env = EnvironmentConfig.load();
        RestAssured.baseURI = env.getApiBaseUrl();
    }

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        driver = DriverFactory.create(env);
        DriverFactory.configureTimeouts(driver, env);
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
