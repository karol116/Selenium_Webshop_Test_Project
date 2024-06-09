package utils;

import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class CookieUtils {

    private List<org.openqa.selenium.Cookie> convertRACookiesToSeleniumCookies(Cookies raCookies) {
        List<org.openqa.selenium.Cookie> seleniumCookies = new ArrayList<>();
        List<io.restassured.http.Cookie> raCookiesList = raCookies.asList();

        for (io.restassured.http.Cookie raCookie : raCookiesList) {
            seleniumCookies.add(
                    new org.openqa.selenium.Cookie(
                            raCookie.getName(),
                            raCookie.getValue(),
                            raCookie.getDomain(),
                            raCookie.getPath(),
                            raCookie.getExpiryDate(),
                            raCookie.isSecured(),
                            raCookie.isHttpOnly()
                    )
            );
        }
        return seleniumCookies;
    }

    public WebDriver addRestAssuredCookiesToDriver(Cookies raCookies, WebDriver driver) {
        List<org.openqa.selenium.Cookie> seleniumCookies = convertRACookiesToSeleniumCookies(raCookies);
        seleniumCookies
                .forEach(
                        sCookie -> driver
                                .manage()
                                .addCookie(sCookie)
                );
        return driver;
    }
}
