package api.actions;

import contants.Endpoint;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AddToCartApi {
    private Cookies cookies;

    public Cookies getCookies() {
        return cookies;
    }

    public AddToCartApi(Cookies cookies) {
//        this.cookies = Optional.ofNullable(cookies).orElse(new Cookies());
        this.cookies = cookies;
    }
    public AddToCartApi() {
        this.cookies = new Cookies();
    }


    public Response addToCart(int productId, int quantity) {
        Header header = new Header("content-Type", "application/x-www-form-urlencoded");
        Headers headers = new Headers(header);

        Map<String, Object> body = new HashMap<>();
        body.put("product_id", productId);
        body.put("quantity", String.valueOf(quantity));

        Response response = Request.post(Endpoint.ADD_TO_CART, cookies, headers, body);

        if (response.getStatusCode() != 200) {
            throw new RuntimeException(
                    "Failed to add product to cart, HTTP status code: " +
                            response.getStatusCode()
            );
        }
        this.cookies = response.getDetailedCookies();
        return response;
    }

}