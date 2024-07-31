package methods;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class TestUtils {
    private WebDriver driver;
    private WebDriverWait wait;
    private WebDriverWait waitBooleanCondition;

    public TestUtils(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofMillis(10000));
        waitBooleanCondition = new WebDriverWait(driver, Duration.ofMillis(5000));
    }

    public void waitUntilElementBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void clickElement(WebElement element) {
        waitUntilElementBeVisible(element);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public String getElementText(WebElement element) {
        waitUntilElementBeVisible(element);
        return element.getText();
    }

    public void insertValue(WebElement element, String value) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(value);
        element.sendKeys(Keys.TAB);
    }


    public void waitForInvisibilityOfOverlays(List<WebElement> overlays) {
        if (!overlays.isEmpty()) {
            wait.until(ExpectedConditions.invisibilityOfAllElements(overlays));
        } else {
            System.err.println("Overlay not found. ");
        }
    }

    public boolean isElementVisible(WebElement element) {
        if (element.isDisplayed()) {
            return true;
        } else {
            try {
                waitBooleanCondition.until(ExpectedConditions.visibilityOf(element));
                return element.isDisplayed();
            } catch (NoSuchElementException e) {
                return false;
            }
        }
    }

}