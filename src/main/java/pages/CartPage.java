package pages;

import methods.TestUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class CartPage extends AbstractPage {
    TestUtils testUtils;

    @FindBy(xpath = "//*[text() = 'Cart totals']/..//*[contains(@class, 'proceed')]")
    private WebElement proceedToCheckoutButton;
    @FindBy(xpath = "//*[@aria-label='Remove this item']")
    private List<WebElement> deleteItemButtons;
    @FindBy(xpath = "//*[@role='alert' and contains(text(), 'removed')]/a")
    private WebElement undoRemoveItem;

    public CartPage(WebDriver driver) {
        super(driver);
        testUtils = new TestUtils(driver);
    }

    public CheckoutPage proceedToCheckout() {
        proceedToCheckoutButton.click();
        waitForTextVisibleOnPage("Checkout");
        return new CheckoutPage(driver);
    }

    public CartPage deleteItem(int itemIndex) throws Exception {
        int numberOfItemsBeforeRemoving = deleteItemButtons.size();
        testUtils.clickElement(deleteItemButtons.get(itemIndex));
        testUtils.waitUntilElementBeVisible(undoRemoveItem);
        int numberOfItemsAfterRemoving = deleteItemButtons.size();


        if (numberOfItemsAfterRemoving >= numberOfItemsBeforeRemoving) {
            throw new Exception("Element nie został usunięty");
        }
        return this;
    }

    public boolean isCartEmptyAfterRemovingItem(String expectedAlert) {
        try {

            String emptyCartMessage = testUtils.getElementText(driver.findElement(By.xpath("//*[text()='Your cart is currently empty.']")));
            return expectedAlert.equals(emptyCartMessage);
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}