package api.actions;

import contants.Endpoint;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import objects.User;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

public class SignupApi {
    private Cookies cookies;

    public Cookies getCookies() {
        return cookies;
    }

    public String getFetchRegisterNonceValueUsingJsoup(){
        Response response = getAccount();
        Document document = Jsoup.parse(response.body().prettyPrint());
        Element element = document.getElementsByAttributeValue("name", "woocommerce-register-nonce").get(0);
        System.out.println(element);
        return element.attr("value");
    }

    public String getFetchLoginNonceValueUsingJsoup(){
        Response response = getAccount();
        Document document = Jsoup.parse(response.body().prettyPrint());
        Element element = document.getElementsByAttributeValue("name", "woocommerce-login-nonce").get(0);
        System.out.println(element);
        return element.attr("value");
    }

    public Response getAccount() {
        Cookies cookies = new Cookies();

        Response response = Request.get(Endpoint.ACCOUNT, cookies);
        if (response.getStatusCode() != 200) {
            throw new RuntimeException(
                    "Failed to fetched the account, HTTP status code: " +
                            response.getStatusCode()
            );
        }
        return response;
    }
    public Response register(User user) {
        Cookies cookies = new Cookies();

        Header header = new Header("content-Type", "application/x-www-form-urlencoded");
        Headers headers = new Headers(header);

        Map<String, Object> body = new HashMap<>();
        body.put("username", user.getUsername());
        body.put("email", user.getEmail());
        body.put("password", user.getPassword());
        body.put("register", "Register");
        body.put("woocommerce-register-nonce", getFetchRegisterNonceValueUsingJsoup());

        Response response = Request.post(Endpoint.ACCOUNT, cookies, headers, body);
        if (response.getStatusCode() != 302) {
            throw new RuntimeException(
                    "Failed to fetched the account, HTTP status code: " +
                            response.getStatusCode()
            );
        }
        this.cookies = response.getDetailedCookies();
        return response;
    }
    public Response logIn(User user) {
        Cookies cookies = new Cookies();

        Header header = new Header("content-Type", "application/x-www-form-urlencoded");
        Headers headers = new Headers(header);

        Map<String, Object> body = new HashMap<>();
        body.put("username", user.getUsername());
        body.put("password", user.getPassword());
        body.put("login", "Log in");
        body.put("woocommerce-login-nonce", getFetchLoginNonceValueUsingJsoup());

        Response response = Request.post(Endpoint.ACCOUNT, cookies, headers,body);
        if (response.getStatusCode() != 302) {
            throw new RuntimeException(
                    "Failed to fetched the account, HTTP status code: " +
                            response.getStatusCode()
            );
        }
        this.cookies = response.getDetailedCookies();
        return response;
    }
}