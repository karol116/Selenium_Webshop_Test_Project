package objects;

import utils.JacksonUtils;

import java.io.IOException;

public class Product {
    private int id;
    private String productName;

    public Product() {
    }

    public Product(int id) throws IOException {
        Product[] products = JacksonUtils.deserializeJson("Products.json", Product[].class);
        for (Product product : products) {
            if(product.getId() == id){
            this.productName = product.getProductName();
            this.id = id;
            }
        }
    }

    public String getProductName() {
        return productName;
    }

    public int getId() {
        return id;
    }
}