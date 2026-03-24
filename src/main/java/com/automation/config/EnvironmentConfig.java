package com.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads {@code config/default.properties} then {@code config/{env}.properties}.
 * System properties (-Dkey=value) override file values when the key exists in the merged set.
 */
public final class EnvironmentConfig {

    private static final String DEFAULT_PROFILE = "default";
    private final Properties props = new Properties();

    private EnvironmentConfig() {
    }

    public static EnvironmentConfig load() {
        String profile = System.getProperty("env", DEFAULT_PROFILE);
        EnvironmentConfig cfg = new EnvironmentConfig();
        cfg.loadClasspath("config/default.properties");
        if (!DEFAULT_PROFILE.equals(profile)) {
            cfg.loadClasspath("config/" + profile + ".properties");
        }
        cfg.applyEnvOverrides();
        cfg.applySystemPropertyOverrides();
        return cfg;
    }

    private void loadClasspath(String path) {
        try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Missing classpath resource: " + path);
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + path, e);
        }
    }

    private void applyEnvOverrides() {
        String grid = System.getenv("SELENIUM_GRID_URL");
        if (grid != null && !grid.isBlank()) {
            props.setProperty("selenium.grid.url", grid.trim());
        }
        String browser = System.getenv("BROWSER");
        if (browser != null && !browser.isBlank()) {
            props.setProperty("browser", browser.trim().toLowerCase());
        }
    }

    private void applySystemPropertyOverrides() {
        Properties sys = System.getProperties();
        for (String name : props.stringPropertyNames()) {
            if (sys.containsKey(name)) {
                props.setProperty(name, sys.getProperty(name));
            }
        }
    }

    public String get(String key) {
        return props.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        String v = props.getProperty(key);
        if (v == null || v.isBlank()) {
            return defaultValue;
        }
        return Integer.parseInt(v.trim());
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String v = props.getProperty(key);
        if (v == null || v.isBlank()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(v.trim());
    }

    public String getWebBaseUrl() {
        return get("web.base.url", "http://localhost:8080");
    }

    public String getApiBaseUrl() {
        return get("api.base.url", getWebBaseUrl());
    }

    public String getGridUrl() {
        return get("selenium.grid.url", "").trim();
    }

    public String getBrowser() {
        return get("browser", "chrome").trim().toLowerCase();
    }

    public boolean isHeadless() {
        return getBoolean("headless", false);
    }

    public int getExplicitWaitSeconds() {
        return getInt("explicit.wait.seconds", 15);
    }

    public int getImplicitWaitSeconds() {
        return getInt("implicit.wait.seconds", 0);
    }

    public int getPageLoadTimeoutSeconds() {
        return getInt("page.load.timeout.seconds", 45);
    }

    public int getScriptTimeoutSeconds() {
        return getInt("script.timeout.seconds", 30);
    }

    public int getFluentWaitTimeoutSeconds() {
        return getInt("fluent.wait.timeout.seconds", 30);
    }

    public long getFluentPollingMillis() {
        return getInt("fluent.wait.polling.millis", 500);
    }

    public boolean isScreenshotOnFailure() {
        return getBoolean("screenshot.on.failure", true);
    }

    public String getScreenshotDir() {
        return get("screenshot.dir", "screenshots");
    }

    public int getRetryMaxAttempts() {
        return getInt("retry.max.attempts", 2);
    }

    public String getBrowserStackHubUrl() {
        return get("browserstack.hub.url", "https://hub-cloud.browserstack.com/wd/hub");
    }

    public String getSauceHubUrl() {
        return get("sauce.hub.url", "https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
    }
}
