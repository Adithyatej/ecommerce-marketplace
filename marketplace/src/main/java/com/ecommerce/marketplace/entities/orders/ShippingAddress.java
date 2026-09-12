package com.ecommerce.marketplace.entities.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Setter;

@Embeddable
public class ShippingAddress {

    @Setter

    private String flatNoOrDoorNo;

    @Setter
    private String street;

    @Setter
    private String area;

    @Setter
    private String city;

    @Setter
    private String state;

    @Setter
    private Long pincode;

}
