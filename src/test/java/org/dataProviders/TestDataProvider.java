package org.dataProviders;

import objects.Product;
import org.testng.annotations.DataProvider;
import utils.JacksonUtils;

import java.io.IOException;

public class TestDataProvider {
    @DataProvider(name = "getFeaturedProducts", parallel = false)
    public Object[] getFeaturedProducts() throws IOException {
        return JacksonUtils.deserializeJson("Products.json", Product[].class);
    }
}
