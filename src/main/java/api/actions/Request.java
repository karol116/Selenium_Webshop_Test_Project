package api.actions;

import contants.Endpoint;
import io.restassured.http.Cookies;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import utils.ConfigLoader;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class Request {
    public static Response get(Endpoint endpoint, Cookies cookies) {
        return given().
                baseUri(ConfigLoader.getInstance().getProperty("baseUrl")).
                cookies(cookies).
                when().
                get(endpoint.url).
                then().log().ifError().
                extract().
                response();
    }

    public static Response post(Endpoint endpoint, Cookies cookies, Headers headers, Map<String, Object> body) {
        return given().
                baseUri(ConfigLoader.getInstance().getProperty("baseUrl")).
                headers(headers).
                formParams(body).
                cookies(cookies).
                when().
                post(endpoint.url).
                then().log().ifError().
                extract().
                response();
    }
}