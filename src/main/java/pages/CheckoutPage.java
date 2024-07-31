package pages;

import methods.TestUtils;
import objects.BillingAddress;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    @FindBy(className = "showcoupon")
    private WebElement showCoupon;

    @FindBy(xpath = "//input[@name = 'coupon_code']")
    private WebElement couponCodeInput;

    @FindBy(xpath = "//button[@name = 'apply_coupon']")
    private WebElement confirmCouponButton;

    @FindBy(xpath = "//*[@class= 'order-total']//*[@class = 'woocommerce-Price-amount amount']/*")
    private WebElement orderTotalAmount;

    @FindBy(xpath = "//*[@class= 'cart-subtotal']//*[@class = 'woocommerce-Price-amount amount']/*")
    private WebElement orderSubtotalAmount;

    @FindBy(xpath = "//*[@data-title= 'Shipping']//*[@class = 'woocommerce-Price-amount amount']/*")
    private WebElement shippingFlatRateAmount;

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

    public CheckoutPage addCoupon(String value) {
        unrollCouponCodeInput();
        testUtils.insertValue(couponCodeInput, value);
        confirmCouponButton.click();
        testUtils.waitForInvisibilityOfOverlays(overlays);
        return this;
    }

    public long countCharacterOccurrences(String sentence, char characterToCount) {
        long characterOccurrencesCounter = 0;
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) == characterToCount) {
                characterOccurrencesCounter++;
            }
        }
        return characterOccurrencesCounter;
    }

    private void unrollCouponCodeInput() {
        if (!couponCodeInput.isDisplayed()) {
            showCoupon.click();
        } else {
            System.err.println("Coupon code input field was visible. ");
        }
    }

    public Double getOrderTotalAmount() {
        String totalAmount = testUtils.getElementText(orderTotalAmount);
        totalAmount = totalAmount.substring(1);//To remove currency
        return Double.valueOf(totalAmount);
    }

    public Double getOrderSubtotalAmount() {
        String totalAmount = testUtils.getElementText(orderSubtotalAmount);
        totalAmount = totalAmount.substring(1);//To remove currency
        return Double.valueOf(totalAmount);
    }

    public Double getShippingFlatRate() {
        String flatRate = testUtils.getElementText(shippingFlatRateAmount);
        flatRate = flatRate.substring(1);//To remove currency

        return Double.valueOf(flatRate);
    }
}