package com.automation.actions;

import com.automation.config.EnvironmentConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Central place for click, type, select, and waits (explicit + fluent).
 */
public final class ElementActions {

    private static final Logger log = LogManager.getLogger(ElementActions.class);

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final EnvironmentConfig env;

    public ElementActions(WebDriver driver, EnvironmentConfig env) {
        this.driver = driver;
        this.env = env;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(env.getExplicitWaitSeconds()));
    }

    public WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click(By locator) {
        try {
            waitClickable(locator).click();
        } catch (ElementClickInterceptedException e) {
            log.warn("Click intercepted, retrying with JS scroll: {}", locator);
            WebElement el = waitVisible(locator);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", el);
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        }
    }

    public void type(By locator, CharSequence text) {
        WebElement el = waitVisible(locator);
        el.clear();
        el.sendKeys(text);
    }

    public void selectByVisibleText(By locator, String text) {
        new Select(waitVisible(locator)).selectByVisibleText(text);
    }

    public void selectByValue(By locator, String value) {
        new Select(waitVisible(locator)).selectByValue(value);
    }

    public String getText(By locator) {
        return waitVisible(locator).getText();
    }

    public boolean isDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Fluent wait for custom conditions (e.g. attribute contains value).
     */
    public WebElement waitFluent(java.util.function.Function<WebDriver, WebElement> condition) {
        FluentWait<WebDriver> fluent = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(env.getFluentWaitTimeoutSeconds()))
                .pollingEvery(Duration.ofMillis(env.getFluentPollingMillis()))
                .ignoring(StaleElementReferenceException.class);
        return fluent.until(condition);
    }

    public List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }
}
