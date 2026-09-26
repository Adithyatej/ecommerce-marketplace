package com.ecommerce.marketplace.projections.products;

import java.math.BigDecimal;

public interface customerCartResponse {

    BigDecimal getPrice();
    Integer getQuantity();
    String getProductName();
    String getBrand();
    String getSellerName();
}
