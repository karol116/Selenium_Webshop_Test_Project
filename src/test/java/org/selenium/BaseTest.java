package org.selenium;

import contants.DriverType;
import factory.DriverManager;
import factory.DriverManagerFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import utils.PropertyUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
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
    public void setUp(DriverType browser, @Optional Method method) {
//        String browser = System.getProperty("browser");//JVM argument
        Properties properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");
        String baseUrl = properties.getProperty("baseUrl");
        if (method.getName().contains("printout")) {
            int i = 0;
        }

        DriverManager driverManager = DriverManagerFactory.selectDriverType(browser);
        setDriver(driverManager.initializeDriver());

        getDriver().get(baseUrl);
        getDriver().manage().window().maximize();
        System.out.println("Current thread: " + Thread.currentThread().getName() + ",\n" + Thread.currentThread().getId());
        System.out.println("Driver: " + getDriver());
    }

    @AfterMethod
    public void afterTest(ITestResult result) throws IOException {
        if(!result.isSuccess()){
            System.out.println("f ");
            File file = new File(
                    "src" + File.separator +
                            result.getTestClass().getRealClass().getSimpleName() + File.separator +
                            result.getMethod().getMethodName()  + "2.png"
                    );
//            takeScreenshot(file);
            takeFullWebPageScreenshot(file);
        }

        Set<String> windowHandles = getDriver().getWindowHandles();
        for (String windowHandle : windowHandles) {
            getDriver().switchTo().window(windowHandle);
            getDriver().close();
        }

        System.out.println("Current thread: " + Thread.currentThread().getName() + ",\n" + Thread.currentThread().getId());
        System.out.println("Driver: " + getDriver());
        System.out.println("Test running time: " + result.getStatus());

        getDriver().quit();
    }

    private void takeScreenshot(File destinationFile) throws IOException {
        TakesScreenshot takesScreenshot = (TakesScreenshot) getDriver();
        File file = takesScreenshot.getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(file, destinationFile);
    }
    private void takeFullWebPageScreenshot(File destinationFile) throws IOException {
        BufferedImage screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
//                .addIgnoredElement(By.id("payment_method_bacs"))
                .takeScreenshot(getDriver())
                .getImage();

        ImageIO.write(screenshot, "PNG", destinationFile);

    }
}