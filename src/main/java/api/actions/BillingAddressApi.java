package api.actions;

import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import objects.BillingAddress;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import utils.ConfigLoader;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BillingAddressApi {
    private Cookies cookies;

    public BillingAddressApi(Cookies cookies) {
        this.cookies = cookies;
    }

    public Cookies getCookies() {
        return cookies;
    }

    public Response getAddress() {
        Response response = given().
                baseUri(ConfigLoader.getInstance().getProperty("baseUrl")).
                cookies(cookies).log().all().
                when().
                get("/account/edit-address/billing").
                then().
                extract().
                response();
        if (response.getStatusCode() != 200) {
            throw new RuntimeException(
                    "Failed to fetched the account, HTTP status code: " +
                            response.getStatusCode()
            );
        }
        return response;
    }

    private String getFetchEditAddressNonceValueUsingJsoup() {
        Response response = getAddress();
        Document document = Jsoup.parse(response.body().prettyPrint());
        Element element = document.getElementsByAttributeValue("name", "woocommerce-edit-address-nonce").get(0);
        return element.attr("value");
    }

    public Response fillBillingAddress(BillingAddress billingAddress) {

        Header header = new Header("Content-Type", "application/x-www-form-urlencoded");
//        Header header2 = new Header("Accept-Language", "pl-PL");

        Headers headers = new Headers(header);


        Map<String, Object> body = new HashMap<>();
        body.put("billing_first_name", billingAddress.getFirstName());
        body.put("billing_last_name", billingAddress.getLastName());
        body.put("billing_country", "PL");
        body.put("billing_address_1", billingAddress.getAddress());
        body.put("billing_city", billingAddress.getCity());
        body.put("billing_postcode", billingAddress.getPostcode());
        body.put("billing_phone", "+48768768526");
//        body.put("_wp_http_referer", "/account/edit-address/billing/");
        body.put("woocommerce-edit-address-nonce", getFetchEditAddressNonceValueUsingJsoup());
        body.put("action", "edit_address");
        body.put("save_address", "Save address");
        body.put("billing_email", billingAddress.getEmail());


        Response response = given().
                baseUri(ConfigLoader.getInstance().getProperty("baseUrl")).
                headers(headers).
                formParams(body).
                cookies(cookies).
                log().all().
                when().
                post("/account/edit-address/billing/").
                then().
                log().all().
                extract().
                response();
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

