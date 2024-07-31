package factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;

public class ChromeDriverManager implements DriverManager {


    @Override
    public WebDriver initializeDriver() {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        chromedriver().clearDriverCache().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        return driver;
    }
}