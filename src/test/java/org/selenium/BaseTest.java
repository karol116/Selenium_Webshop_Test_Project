package org.selenium;

import factory.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import utils.PropertyUtils;

import java.util.Properties;
import java.util.Set;

public class BaseTest {
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private void setDriver(WebDriver driver) {
        this.driver.set(driver);
    }

    public WebDriver getDriver() {
        return this.driver.get();
    }

    @Parameters("browser")
    @BeforeMethod
    public void setUp(String browser) {
//        String browser = System.getProperty("browser");//JVM argument
        Properties properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");
        String baseUrl = properties.getProperty("baseUrl");

        DriverManager driverManager = new DriverManager();
        setDriver(driverManager.initializeDriver(browser));

        getDriver().get(baseUrl);
        getDriver().manage().window().maximize();
        System.out.println("Current thread: " + Thread.currentThread().getName() + ",\n" + Thread.currentThread().getId());
        System.out.println("Driver: " + getDriver());
    }

    @AfterMethod
    public void afterTest() {
        Set<String> windowHandles = getDriver().getWindowHandles();
        for (String windowHandle : windowHandles) {
            getDriver().switchTo().window(windowHandle);
            getDriver().close();
        }
        System.out.println("Current thread: " + Thread.currentThread().getName() + ",\n" + Thread.currentThread().getId());
        System.out.println("Driver: " + getDriver());
        getDriver().quit();
    }
}