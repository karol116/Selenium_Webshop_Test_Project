package pages;

import methods.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class AbstractPage {
    protected WebDriver driver;
    WebDriverWait wait;
    TestUtils testUtils;

    @FindBy(xpath = "//li[contains(@id, 'menu-item')]/a[text()='Store' and @class='menu-link']")
    private WebElement storeButton;

    @FindBy(xpath = "//li[contains(@id, 'menu-item')]/a[text()='Account' and @class='menu-link']")
    private WebElement accountButton;

    protected AbstractPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        testUtils = new TestUtils(driver);
        wait = new WebDriverWait(driver, Duration.ofMillis(5000));
    }

    public StorePage goToStorePage() {
        testUtils.clickElement(storeButton);
        return new StorePage(driver);
    }

    public AccountPage goToAccountPage() {
        testUtils.clickElement(accountButton);
        return new AccountPage(driver);
    }

    public Boolean isTextVisibleOnPage(String text) {
        try {
            WebElement element = driver.findElement(By.xpath("//*[text() = '" + text + "']"));
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;

        } catch (NoSuchElementException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public void waitForTextVisibleOnPage(String text) {
        WebElement element = driver.findElement(By.xpath("//*[text() = '" + text + "']"));
        wait.until(ExpectedConditions.visibilityOf(element));
    }
}