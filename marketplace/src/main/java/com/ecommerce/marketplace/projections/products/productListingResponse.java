package com.ecommerce.marketplace.projections.products;

import java.math.BigDecimal;

public interface productListingResponse {

    BigDecimal getPrice();
    Integer getStockQuantity();
    String getProductName();
    String getBrand();
    String getStoreName();

}