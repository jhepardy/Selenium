package com.automation.pages.internet;

import com.automation.actions.ElementActions;
import com.automation.config.EnvironmentConfig;
import com.automation.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * https://the-internet.herokuapp.com/login — stable demo for CI.
 */
public class TheInternetLoginPage extends BasePage {

    private static final String PATH = "/login";

    private final By username = By.id("username");
    private final By password = By.id("password");
    private final By submit = By.cssSelector("button.radius");
    private final By flashMessage = By.id("flash");

    private final ElementActions actions;

    public TheInternetLoginPage(WebDriver driver, EnvironmentConfig env) {
        super(driver, env);
        this.actions = new ElementActions(driver, env);
    }

    public TheInternetLoginPage open() {
        driver.get(env().getWebBaseUrl() + PATH);
        return this;
    }

    public TheInternetLoginPage enterCredentials(String user, String pass) {
        actions.type(username, user);
        actions.type(password, pass);
        return this;
    }

    public void submitLogin() {
        actions.click(submit);
    }

    public String flashBannerText() {
        return actions.getText(flashMessage);
    }

    public boolean isOnLoginPage() {
        return actions.isDisplayed(username);
    }
}
