package pages;

import methods.TestUtils;
import objects.BillingAddress;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import utils.PropertyUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class CheckoutPage extends AbstractPage {
    Properties properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");

    @FindBy(xpath = "//select[@id='billing_country']")
    private WebElement countrySelect;

    @FindBy(id = "place_order")
    private WebElement placeOrderButton;

    @FindBy(id = "payment_method_bacs")
    private WebElement directBankTransferRadioButton;
    @FindBy(xpath = "//h2[text()='Billing address']/..//address")
    private WebElement clientAddressBillingAddressSection;

    @FindBy(xpath = "//*[text()='Billing details']/..//label/*[@class='required']/../..//input")
    private List<WebElement> requiredFormInputs;

    public CheckoutPage(WebDriver driver) {
        super(driver);
        testUtils = new TestUtils(driver);
    }

    public CheckoutPage openCheckoutPage() {
        driver.navigate().to(properties.getProperty("baseUrl") + "/checkout/");
        return new CheckoutPage(driver);
    }

    public CheckoutPage fillBillingForm(BillingAddress billingAddress) {
        Select countrySel = new Select(countrySelect);
        countrySel.selectByVisibleText("Poland");

        Map<String, String> formInputData = new HashMap<>();
        formInputData.put("billing_first_name", billingAddress.getFirstName());
        formInputData.put("billing_last_name", billingAddress.getLastName());
        formInputData.put("billing_address_1", billingAddress.getAddress());
        formInputData.put("billing_postcode", billingAddress.getPostcode());
        formInputData.put("billing_city", billingAddress.getCity());
        formInputData.put("billing_email", billingAddress.getEmail());

        for (WebElement inputField : requiredFormInputs) {
            if (formInputData.containsKey(inputField.getAttribute("id"))) {
                inputField.clear();
                inputField.sendKeys(formInputData.get(inputField.getAttribute("id")));
            }
        }
        return this;
    }

    public CheckoutPage selectDirectBankTransfer() {
        wait.until(ExpectedConditions.invisibilityOfAllElements(driver.findElements(By.xpath("//*[@class='blockUI blockMsg blockElement']"))));
        if (!directBankTransferRadioButton.isSelected()) {
            directBankTransferRadioButton.click();
        }
        return this;
    }

    public CheckoutPage clickPlaceOrder() throws InterruptedException {
        testUtils.waitForInvisibilityOfOverlays(overlays);
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderButton));
        Thread.sleep(3000);
        placeOrderButton.click();
        wait.until(ExpectedConditions.invisibilityOf(placeOrderButton));
        return this;
    }

    public String getClientAddressBillingAddressSection() {
        return testUtils.getElementText(clientAddressBillingAddressSection);
    }
}