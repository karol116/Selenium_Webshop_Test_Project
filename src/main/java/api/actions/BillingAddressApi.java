package api.actions;

import contants.Endpoint;
import io.restassured.http.Cookies;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import objects.BillingAddress;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.HashMap;
import java.util.Map;

public class BillingAddressApi {
    private Cookies cookies;

    public BillingAddressApi(Cookies cookies) {
        this.cookies = cookies;
    }

    public Cookies getCookies() {
        return cookies;
    }

    public Response getAddress() {
        Response response = Request.get(Endpoint.EDIT_BILLING_ADDRESS, cookies);
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
//        Header header2 = new Header("Accept-Language", "pl-PL");//FIXME: enable entering of polish letters
        Headers headers = new Headers(header);

        Map<String, Object> body = new HashMap<>();
        body.put("billing_first_name", billingAddress.getFirstName());
        body.put("billing_last_name", billingAddress.getLastName());
        body.put("billing_country", "PL");
        body.put("billing_address_1", billingAddress.getAddress());
        body.put("billing_city", billingAddress.getCity());
        body.put("billing_postcode", billingAddress.getPostcode());
        body.put("woocommerce-edit-address-nonce", getFetchEditAddressNonceValueUsingJsoup());
        body.put("action", "edit_address");
        body.put("save_address", "Save address");
        body.put("billing_email", billingAddress.getEmail());

        Response response = Request.post(Endpoint.EDIT_BILLING_ADDRESS, cookies, headers, body);
        if (response.getStatusCode() != 302) {
            throw new RuntimeException(
                    "Failed to fill billing address, HTTP status code: " +
                            response.getStatusCode()
            );
        }
        this.cookies = response.getDetailedCookies();
        return response;
    }
}