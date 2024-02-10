package pages;

import methods.TestUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class AccountPage extends AbstractPage {

    TestUtils testUtils;

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
        String userName = "SW3P";//TODO:TestEnvironment.getCustomerUsername();
        String password = "pass!wS1";//TODO:TestEnvironment.getCustomerPassword();

        usernameLoginInput.sendKeys(userName);
        passwordLoginInput.sendKeys(password);
        logInButton.click();
    }
}

