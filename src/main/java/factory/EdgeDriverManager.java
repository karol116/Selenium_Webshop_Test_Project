package factory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import static io.github.bonigarcia.wdm.WebDriverManager.edgedriver;

public class EdgeDriverManager implements DriverManager {

    EdgeOptions edgeOptions;

    @Override
    public WebDriver initializeDriver() {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        edgedriver().cachePath("Drivers").setup();
        edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--remote-allow-origins=*");

        WebDriver driver = new EdgeDriver(edgeOptions);
        driver.manage().window().maximize();
        return driver;
    }
}