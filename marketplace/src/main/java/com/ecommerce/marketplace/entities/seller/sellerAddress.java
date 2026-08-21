package com.ecommerce.marketplace.entities.seller;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class sellerAddress {


    private String doorNumber;
    private String street;
    private String city;
    private String state;
    private Long pincode;
}
