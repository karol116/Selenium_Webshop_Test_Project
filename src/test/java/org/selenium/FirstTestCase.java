package org.selenium;

import api.actions.AddToCartApi;
import api.actions.SignupApi;
import com.github.javafaker.Faker;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import objects.BillingAddress;
import objects.Product;
import objects.User;
import org.openqa.selenium.Cookie;
import org.testng.annotations.Test;
import pages.*;
import utils.CookieUtils;
import utils.FakerUtils;
import utils.JacksonUtils;
import utils.PropertyUtils;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.assertFalse;
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

    //Otwarcie strony przez drivera z utworzonym i zalogowanym użytkownikiem oraz dodanym produktem
    @Test
    public void deleteItemFromCart2() throws Exception {
        Properties properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");
        String baseUrl = properties.getProperty("baseUrl");
        //RestAssured
        CookieUtils cookieUtils = new CookieUtils();
        String userName = "testUserName" + new FakerUtils().generateRandomumber();
        MainPage mainPage = new MainPage(getDriver());
        mainPage.goToAccountPage();
        User user = new User()
                .setUsername(userName)
                .setEmail(userName + "@mail.it")
                .setPassword("stronPass3");

        SignupApi signupApi = new SignupApi();
        Response registerValue = signupApi.register(user);

        AddToCartApi addToCartApi = new AddToCartApi();
        addToCartApi.addToCart("1196", 1);
        Cookies raCookies = addToCartApi.getCookies();

        //RestAssured -> Selenium
        List<Cookie> seleniumCookies = cookieUtils.convertRACookiesToSeleniumCookies(raCookies);
        seleniumCookies
                .forEach(
                        sCookie -> getDriver()
                        .manage()
                        .addCookie(sCookie)
                );

        //Selenium
        CartPage cartPage = new CartPage(getDriver());
        getDriver().navigate().to(baseUrl + "/cart");
        assertFalse(cartPage.isCartEmptyAfterRemovingItem("Your cart is currently empty."));
        cartPage.deleteItem(0);
        assertTrue(cartPage.isCartEmptyAfterRemovingItem("Your cart is currently empty."));
    }
}