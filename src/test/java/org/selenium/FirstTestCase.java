package org.selenium;

import objects.BillingAddress;
import objects.Product;
import org.junit.Test;
import org.testng.AssertJUnit;
import pages.*;
import utils.JacksonUtils;

import javax.management.InstanceNotFoundException;
import java.io.IOException;

import static org.testng.AssertJUnit.assertTrue;


public class FirstTestCase extends BaseTest {


    @Test
    public void buyItemWithLogIn() throws InstanceNotFoundException, InterruptedException, IOException {
        BillingAddress billingAddress = JacksonUtils.deserializeJson("BillingAddress.json", BillingAddress.class);
        Product product = new Product(1215);
        String searchedText = product.getProductName().split(" ")[0];//fikołki
        MainPage mainPage = new MainPage(driver);

        AccountPage accountPage = mainPage.goToAccountPage();
        accountPage.logInAsCustomer();
        AssertJUnit.assertTrue(accountPage.isTextVisibleOnPage("Logout"));

        StorePage storePage = accountPage.goToStorePage();
        storePage.searchInsertedText(searchedText);
        String searchResultsText = storePage.getSearchResultsText();
        AssertJUnit.assertTrue(searchResultsText.contains(searchedText));

        storePage.addProductToCart(product.getProductName());
        CartPage cartPage = storePage.goToCart();

        CheckoutPage checkoutPage = cartPage.proceedToCheckout();
        checkoutPage.
                fillBillingForm(billingAddress).
                selectDirectBankTransfer();

        AssertJUnit.assertTrue(checkoutPage.isTextVisibleOnPage("Thank you. Your order has been received."));
    }

    @Test
    public void deleteItemFromCart() throws Exception {
        MainPage mainPage = new MainPage(driver);

        AccountPage accountPage = mainPage.goToAccountPage();
        accountPage.logInAsCustomer();
        AssertJUnit.assertTrue(accountPage.isTextVisibleOnPage("Logout"));

        StorePage storePage = accountPage.goToStorePage();

        CartPage cartPage = storePage
                .addFirstDisplayedProductToCart()
                .goToCart();
        cartPage.deleteItem(0);
        AssertJUnit.assertTrue(cartPage.isCartEmptyAfterRemovingItem("Your cart is currently empty."));
    }
}