package org.selenium;

import factory.DriverManager;
import objects.BillingAddress;
import objects.Product;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;


import java.util.Set;

import static io.github.bonigarcia.wdm.WebDriverManager.edgedriver;

public class BaseTest {
    WebDriver driver;
    EdgeOptions options = new EdgeOptions();
//    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

//    private void setDriver(WebDriver driver) {
//        this.driver.set(driver);
//    }

//    public WebDriver getDriver() {
//        return this.driver.get();
//    }

    //    @Parameters("browser")
//    @BeforeMethod
    @Before
    public void setUp() {
        String browser = System.getProperty("browser");//JVM argument

        DriverManager driverManager = new DriverManager();
        edgedriver().cachePath("Drivers").setup();


//        driver = new EdgeDriver(options);
        driver = driverManager.initializeDriver(browser);

        driver.get("https://www.askomdch.com");
        driver.manage().window().maximize();
        System.out.println("Current thread: " + Thread.currentThread().getName() + ",\n" + Thread.currentThread().getId());
//        System.out.println("Driver: " + getDriver());
    }

    //    @AfterMethod
    @After
    public void afterTest() {
        Set<String> windowHandles = driver.getWindowHandles();
        for (String windowHandle : windowHandles) {
            driver.switchTo().window(windowHandle);
            driver.close();
        }
        System.out.println("Current thread: " + Thread.currentThread().getName() + ",\n" + Thread.currentThread().getId());
//        System.out.println("Driver: " + getDriver());
        driver.quit();
    }
}
