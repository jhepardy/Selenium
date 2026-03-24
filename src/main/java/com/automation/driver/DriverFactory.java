package com.automation.driver;

import com.automation.config.EnvironmentConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates local or {@link RemoteWebDriver} from environment.
 * Cloud: set {@code selenium.grid.url} to BrowserStack/Sauce hub and pass caps via system properties
 * or extend this factory for your vault-backed secrets.
 */
public final class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static WebDriver create(EnvironmentConfig env) {
        applyTimeoutsShell(env);
        String grid = env.getGridUrl();
        if (!grid.isEmpty()) {
            return createRemote(env, grid);
        }
        return createLocal(env);
    }

    private static void applyTimeoutsShell(EnvironmentConfig env) {
        // Actual timeouts applied after driver is created in tests or a small helper if needed
        log.debug("Browser={}, headless={}", env.getBrowser(), env.isHeadless());
    }

    private static WebDriver createLocal(EnvironmentConfig env) {
        String browser = env.getBrowser();
        return switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions o = new FirefoxOptions();
                if (env.isHeadless()) {
                    o.addArguments("-headless");
                }
                yield new FirefoxDriver(o);
            }
            case "edge" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions o = new EdgeOptions();
                if (env.isHeadless()) {
                    o.addArguments("--headless=new");
                }
                yield new EdgeDriver(o);
            }
            case "safari" -> {
                SafariOptions o = new SafariOptions();
                yield new SafariDriver(o);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions o = new ChromeOptions();
                if (env.isHeadless()) {
                    o.addArguments("--headless=new");
                }
                o.addArguments("--window-size=1920,1080");
                o.addArguments("--disable-gpu", "--no-sandbox");
                yield new ChromeDriver(o);
            }
        };
    }

    private static WebDriver createRemote(EnvironmentConfig env, String gridUrl) {
        Capabilities caps = buildRemoteCapabilities(env);
        try {
            return new RemoteWebDriver(URI.create(gridUrl).toURL(), caps, false);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid selenium.grid.url: " + gridUrl, e);
        }
    }

    private static Capabilities buildRemoteCapabilities(EnvironmentConfig env) {
        String browser = env.getBrowser();
        return switch (browser) {
            case "firefox" -> {
                FirefoxOptions o = new FirefoxOptions();
                o.setCapability("browserName", "firefox");
                if (env.isHeadless()) {
                    o.addArguments("-headless");
                }
                yield mergeGridMetadata(o, env);
            }
            case "edge" -> {
                EdgeOptions o = new EdgeOptions();
                if (env.isHeadless()) {
                    o.addArguments("--headless=new");
                }
                yield mergeGridMetadata(o, env);
            }
            case "safari" -> {
                SafariOptions o = new SafariOptions();
                yield mergeGridMetadata(o, env);
            }
            default -> {
                ChromeOptions o = new ChromeOptions();
                if (env.isHeadless()) {
                    o.addArguments("--headless=new");
                }
                o.addArguments("--window-size=1920,1080");
                yield mergeGridMetadata(o, env);
            }
        };
    }

    /**
     * Example: -Dbrowserstack.project=MyApp -Dbrowserstack.build=123 (optional; extend as needed).
     */
    private static <T extends org.openqa.selenium.remote.AbstractDriverOptions<?>> T mergeGridMetadata(
            T options, EnvironmentConfig env) {
        Map<String, Object> bs = new HashMap<>();
        String project = System.getProperty("browserstack.project");
        if (project != null) {
            bs.put("project", project);
        }
        String build = System.getProperty("browserstack.build");
        if (build != null) {
            bs.put("build", build);
        }
        if (!bs.isEmpty()) {
            options.setCapability("bstack:options", bs);
        }
        String sauceName = System.getProperty("sauce.name");
        if (sauceName != null) {
            Map<String, Object> sauceOpts = new HashMap<>();
            sauceOpts.put("name", sauceName);
            options.setCapability("sauce:options", sauceOpts);
        }
        return options;
    }

    public static void configureTimeouts(WebDriver driver, EnvironmentConfig env) {
        var timeouts = driver.manage().timeouts();
        int implicit = env.getImplicitWaitSeconds();
        if (implicit > 0) {
            timeouts.implicitlyWait(java.time.Duration.ofSeconds(implicit));
        } else {
            timeouts.implicitlyWait(java.time.Duration.ZERO);
        }
        timeouts.pageLoadTimeout(java.time.Duration.ofSeconds(env.getPageLoadTimeoutSeconds()));
        timeouts.scriptTimeout(java.time.Duration.ofSeconds(env.getScriptTimeoutSeconds()));
    }
}
