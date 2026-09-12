package com.ecommerce.marketplace.entities.seller;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class sellerAddress {



    @Column(name = "door_number",nullable = true)
    private String doorNumber;
    @Column(name = "street",nullable = true)
    private String street;
    @Column(name = "city",nullable = true)
    private String city;
    @Column(name = "state",nullable = true)
    private String state;
    @Column(name = "pincode",nullable = true)
    private Long pinCode;
}
