package org.selenium;

import objects.BillingAddress;
import objects.Product;
import org.testng.annotations.Test;
import pages.*;
import utils.JacksonUtils;

import javax.management.InstanceNotFoundException;
import java.io.IOException;

import static org.testng.Assert.assertTrue;

public class FirstTestCase extends BaseTest {

    @Test
    public void buyItemWithLogIn() throws InstanceNotFoundException, InterruptedException, IOException {
        BillingAddress billingAddress = JacksonUtils.deserializeJson("BillingAddress.json", BillingAddress.class);
        Product product = new Product(1215);
        String searchedText = product.getProductName().split(" ")[0];//fikołki
        MainPage mainPage = new MainPage(getDriver());

        AccountPage accountPage = mainPage.goToAccountPage();
        accountPage.logInAsCustomer();
        assertTrue(accountPage.isTextVisibleOnPage("Logout"));

        StorePage storePage = accountPage.goToStorePage();
        storePage.searchInsertedText(searchedText);
        String searchResultsText = storePage.getSearchResultsText();
        assertTrue(searchResultsText.contains(searchedText));

        storePage.addProductToCart(product.getProductName());
        CartPage cartPage = storePage.goToCart();

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.
                fillBillingForm(billingAddress).
                selectDirectBankTransfer();
        assertTrue(checkoutPage.isTextVisibleOnPage("Thank you. Your order has been received."));
    }

    @Test
    public void deleteItemFromCart() throws Exception {
        MainPage mainPage = new MainPage(getDriver());

//        Calendar calendar = Calendar.getInstance();
//        int day = calendar.get(Calendar.DAY_OF_WEEK);
//        System.out.println(day);

        AccountPage accountPage = mainPage.goToAccountPage();
        accountPage.logInAsCustomer();
        assertTrue(accountPage.isTextVisibleOnPage("Logout"));

        StorePage storePage = accountPage.goToStorePage();

        CartPage cartPage = storePage
                .addFirstDisplayedProductToCart()
                .goToCart();
        cartPage.deleteItem(0);
        assertTrue(cartPage.isCartEmptyAfterRemovingItem("Your cart is currently empty."));
    }
}