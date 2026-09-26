package com.ecommerce.marketplace.projections.products;

public interface OrdersResponse {
    Long getOrderId();

    Long getOrderItemId();

    String getProductName();

    String getBrand();

    String getSeller();

    String getStatus();



}
