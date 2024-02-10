package methods;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TestUtils {
    WebDriver driver;
    WebDriverWait wait;

    public TestUtils(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofMillis(5000));
    }

    public void waitUntilElementBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void clickElement(WebElement element) {
        waitUntilElementBeVisible(element);
        element.click();
    }

    public String getElementText(WebElement element) {
        waitUntilElementBeVisible(element);
        return element.getText();
    }

    public void clickElementOptional(WebElement element) {
        try {
            clickElement(element);
        } catch (ElementNotInteractableException e) {
            System.out.println("Element not found" + element.getText());
        }
    }
}