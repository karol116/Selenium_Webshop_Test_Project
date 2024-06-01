package api.actions;

import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import utils.ConfigLoader;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AddToCartApi {
    private Cookies cookies;

    public Cookies getCookies() {
        return cookies;
    }

    public Response getCourt() {
        Cookies cookies = new Cookies();

        Response response = given().
                baseUri(ConfigLoader.getInstance().getProperty("baseUrl")).
                cookies(cookies).
                log().all().
                when().
                get("/store").
                then().
                log().all().
                extract().
                response();
        if (response.getStatusCode() != 200) {
            throw new RuntimeException(
                    "Failed to fetched the court, HTTP status code: " +
                            response.getStatusCode()
            );
        }
        return response;
    }

    public Response addToCart(String productId, int quantity) {
        Cookies cookies = new Cookies();
        Header header = new Header("content-Type", "application/x-www-form-urlencoded");
        Headers headers = new Headers(header);

        Map<String, String> body = new HashMap<>();
        body.put("product_id", productId);
        body.put("quantity", String.valueOf(quantity));

        Response response = given().
                baseUri(ConfigLoader.getInstance().getProperty("baseUrl") + "/store").
                headers(headers).
                formParams(body).
                cookies(cookies).
                log().all().
                when().
                post("/?wc-ajax=add_to_cart").//sqlInjectionTest 1234
                        then().
                log().all().
                extract().
                response();
        if (response.getStatusCode() != 200) {
            throw new RuntimeException(
                    "Failed to fetched the account, HTTP status code: " +
                            response.getStatusCode()
            );
        }
        this.cookies = response.getDetailedCookies();
        return response;
    }
}

