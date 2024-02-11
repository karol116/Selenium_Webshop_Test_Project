package pages;

import methods.TestUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import utils.PropertyUtils;

import java.util.Properties;

public class AccountPage extends AbstractPage {
    Properties properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");

    @FindBy(xpath = "//h2[text()='Login']/..//input[@name='username']")
    private WebElement usernameLoginInput;

    @FindBy(xpath = "//h2[text()='Login']/..//input[@name='password']")
    private WebElement passwordLoginInput;

    @FindBy(xpath = "//button[@name='login']")
    private WebElement logInButton;

    public AccountPage(WebDriver driver) {
        super(driver);
        testUtils = new TestUtils(driver);
    }

    public void logInAsCustomer() {
        String userName = properties.getProperty("userName");
        String password = properties.getProperty("password");

        usernameLoginInput.sendKeys(userName);
        passwordLoginInput.sendKeys(password);
        logInButton.click();
    }
}