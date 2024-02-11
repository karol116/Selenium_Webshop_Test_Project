package factory;

import contants.DriverType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import static io.github.bonigarcia.wdm.WebDriverManager.*;

public class DriverManager {

    public WebDriver initializeDriver(String browser) {
        WebDriver driver;
        EdgeOptions edgeOptions;
        System.setProperty("webdriver.http.factory", "jdk-http-client");

        switch (DriverType.valueOf(browser)) {
            case CHROME: {
                chromedriver().cachePath("Drivers").setup();
                driver = new ChromeDriver();
                break;
            }
            case FIREFOX: {
                firefoxdriver().cachePath("Drivers").setup();
                driver = new FirefoxDriver();
                break;
            }
            case EDGE: {
                edgedriver().cachePath("Drivers").setup();
                edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--remote-allow-origins=*");
                driver = new EdgeDriver(edgeOptions);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + DriverType.valueOf(browser));
        }
        return driver;
    }
}