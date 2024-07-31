package org.selenium;

import api.actions.AddToCartApi;
import api.actions.BillingAddressApi;
import api.actions.SignupApi;
import io.restassured.http.Cookies;
import objects.BillingAddress;
import objects.Product;
import objects.User;
import org.testng.annotations.Test;
import pages.*;
import utils.CookieUtils;
import utils.JacksonUtils;

import javax.management.InstanceNotFoundException;
import java.io.IOException;

import static org.testng.Assert.assertTrue;

public class StoreTestSuite extends BaseTest {
    @Test
    public void placeOrderAsRegularCustomer() throws Exception {
        User user = User.getDefaultUser();
        BillingAddress billingAddress = BillingAddress.getDefaultBillingAddress(user);

        //RestAssured
        SignupApi signupApi = new SignupApi();
        signupApi.register(user);
        Cookies raRegisterCookies = signupApi.getCookies();

        BillingAddressApi billingAddressApi = new BillingAddressApi(raRegisterCookies);
        billingAddressApi.fillBillingAddress(billingAddress);

        AddToCartApi addToCartApi = new AddToCartApi(raRegisterCookies);
        addToCartApi.addToCart(1196, 1);

        //RestAssured -> Selenium
        CookieUtils cookieUtils = new CookieUtils();
        cookieUtils.addRestAssuredCookiesToDriver(raRegisterCookies, getDriver());

        //Selenium
        CheckoutPage checkoutPage = new CheckoutPage(getDriver()).openCheckoutPage();
        checkoutPage
                .selectDirectBankTransfer()
                .clickPlaceOrder();
        String clientAddress = checkoutPage.getClientAddressBillingAddressSection();

        assertTrue(checkoutPage.isTextVisibleOnPage("Thank you. Your order has been received."));
        assertTrue(clientAddress.contains(billingAddress.getFirstName() + " " + billingAddress.getLastName()));
        checkoutPage.countCharacterOccurrences(clientAddress, 'e');


    }

    @Test
    public void buyItemWithLogIn() throws InstanceNotFoundException, InterruptedException, IOException {
        BillingAddress billingAddress = JacksonUtils.deserializeJson("BillingAddress.json", BillingAddress.class);
        Product product = new Product(1215);
        String searchedText = product.getProductName().split(" ")[0];
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
                clickPlaceOrder();
        assertTrue(checkoutPage.isTextVisibleOnPage("Thank you. Your order has been received."));
    }

    @Test
    public void deleteItemFromCart() throws Exception {
        MainPage mainPage = new MainPage(getDriver());

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