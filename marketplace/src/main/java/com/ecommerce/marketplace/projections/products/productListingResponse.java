package com.ecommerce.marketplace.projections.products;

public interface productListingResponse {

    Double getPrice();
    Integer getStockQuantity();
    String getProductName();
    String getBrand();
    String getSellerName();

}
