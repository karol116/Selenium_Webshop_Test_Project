package utils;

import io.restassured.http.Cookie;
import io.restassured.http.Cookies;

import java.util.ArrayList;
import java.util.List;

public class CookieUtils {

    public List<org.openqa.selenium.Cookie> convertRACookiesToSeleniumCookies(Cookies raCookies) {
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
}
