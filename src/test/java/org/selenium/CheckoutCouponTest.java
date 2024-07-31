package org.selenium;

import api.actions.AddToCartApi;
import io.restassured.http.Cookies;
import objects.Product;
import org.dataProviders.TestDataProvider;
import org.testng.annotations.Test;
import pages.CheckoutPage;
import utils.CookieUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CheckoutCouponTest extends BaseTest {

    @Test(dataProvider = "getFeaturedProducts", dataProviderClass = TestDataProvider.class)
    public void addFreeShippingCoupon(Product product) throws Exception {
        String couponName = "freeship";

        //RestAssured
        AddToCartApi addToCartApi = new AddToCartApi();
        addToCartApi.addToCart(product.getId(), 1);
        Cookies raCookies = addToCartApi.getCookies();

        //RestAssured -> Selenium
        CookieUtils cookieUtils = new CookieUtils();
        cookieUtils.addRestAssuredCookiesToDriver(raCookies, getDriver());

        //Selenium
        CheckoutPage checkoutPage = new CheckoutPage(getDriver()).openCheckoutPage();
        Double orderTotalAmountWithoutCoupon = checkoutPage.getOrderTotalAmount();
        Double shippingFlatRate = checkoutPage.getShippingFlatRate();

        checkoutPage.addCoupon(couponName);
        System.out.println();

        Double orderTotalAmountWithCoupon = checkoutPage.getOrderTotalAmount();
        Double expectedOrderTotalAmountWithCoupon = orderTotalAmountWithoutCoupon - shippingFlatRate;

        assertEquals(expectedOrderTotalAmountWithCoupon, orderTotalAmountWithCoupon);
        assertTrue("Total amount is not above 0", orderTotalAmountWithCoupon > 0);
    }

    @Test(dataProvider = "getFeaturedProducts", dataProviderClass = TestDataProvider.class)
    public void add5DollarDiscountOfCourtCoupon(Product product) throws Exception {
        String couponName = "offcart5";

        //RestAssured
        AddToCartApi addToCartApi = new AddToCartApi();
        addToCartApi.addToCart(product.getId(), 1);
        Cookies raCookies = addToCartApi.getCookies();

        //RestAssured -> Selenium
        CookieUtils cookieUtils = new CookieUtils();
        cookieUtils.addRestAssuredCookiesToDriver(raCookies, getDriver());

        //Selenium
        CheckoutPage checkoutPage = new CheckoutPage(getDriver()).openCheckoutPage();
        Double orderSubtotalAmountWithoutCoupon = checkoutPage.getOrderSubtotalAmount();

        checkoutPage.addCoupon(couponName);
        System.out.println();

        Double orderTotalAmountWithCoupon = checkoutPage.getOrderTotalAmount();
        Double expectedOrderTotalAmountWithCoupon = orderSubtotalAmountWithoutCoupon - 5;

        assertEquals(expectedOrderTotalAmountWithCoupon, orderTotalAmountWithCoupon);
        assertTrue("Total amount is not above 0", orderTotalAmountWithCoupon > 0);
    }
}