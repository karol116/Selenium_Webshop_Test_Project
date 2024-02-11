package pages;

import methods.TestUtils;
import org.openqa.selenium.WebDriver;

public class MainPage extends AbstractPage {
    TestUtils testUtils;

    public MainPage(WebDriver driver) {
        super(driver);
        testUtils = new TestUtils(driver);
    }
}