package com.automation.tests.internet;

import com.automation.api.SampleApiClient;
import com.automation.data.CsvDataLoader;
import com.automation.data.JsonDataLoader;
import com.automation.data.LoginCredentials;
import com.automation.pages.internet.TheInternetLoginPage;
import com.automation.tests.base.AbstractWebTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class TheInternetLoginTest extends AbstractWebTest {

    @Test(groups = {"smoke", "login"})
    public void shouldReachLoginPage() {
        new TheInternetLoginPage(driver, env).open();
        Assert.assertTrue(driver.getCurrentUrl().contains("/login"), "Expected login URL");
    }

    @Test(groups = {"smoke", "login", "regression"}, dataProvider = "jsonUsers")
    public void shouldLoginWithValidCredentialsFromJson(String username, String password) {
        TheInternetLoginPage page = new TheInternetLoginPage(driver, env).open();
        page.enterCredentials(username, password).submitLogin();
        Assert.assertTrue(driver.getCurrentUrl().contains("/secure"), "Expected secure area URL");
        Assert.assertTrue(page.flashBannerText().toLowerCase().contains("secure area"));
    }

    @Test(groups = {"regression", "login"}, dataProvider = "csvUsers")
    public void shouldLoginWithValidCredentialsFromCsv(String username, String password) {
        new TheInternetLoginPage(driver, env)
                .open()
                .enterCredentials(username, password)
                .submitLogin();
        Assert.assertTrue(driver.getCurrentUrl().contains("/secure"));
    }

    @Test(groups = {"smoke", "hybrid"})
    public void hybridApiHealthCheck() {
        var api = new SampleApiClient(env);
        int code = api.get("/login").getStatusCode();
        Assert.assertEquals(code, 200, "Login page should respond over HTTP");
    }

    @DataProvider(name = "jsonUsers")
    public Object[][] jsonUsers() throws IOException {
        List<LoginCredentials> list = JsonDataLoader.loadLoginUsers("testdata/users.json");
        Object[][] data = new Object[list.size()][2];
        for (int i = 0; i < list.size(); i++) {
            LoginCredentials c = list.get(i);
            data[i][0] = c.getUsername();
            data[i][1] = c.getPassword();
        }
        return data;
    }

    @DataProvider(name = "csvUsers")
    public Object[][] csvUsers() throws Exception {
        List<LoginCredentials> list = CsvDataLoader.loadLoginUsers("testdata/users.csv");
        Object[][] data = new Object[list.size()][2];
        for (int i = 0; i < list.size(); i++) {
            LoginCredentials c = list.get(i);
            data[i][0] = c.getUsername();
            data[i][1] = c.getPassword();
        }
        return data;
    }
}
