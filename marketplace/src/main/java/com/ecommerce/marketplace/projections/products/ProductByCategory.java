package com.ecommerce.marketplace.projections.products;

public interface ProductByCategory {

    Long getId();

    String getMainProductCategory();

    String getSubProductCategory();

    String getProductName();

    String getBrand();

    String getProductDescription();

    String getPrice();
}
