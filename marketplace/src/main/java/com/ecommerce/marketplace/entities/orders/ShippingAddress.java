package com.ecommerce.marketplace.entities.orders;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Embeddable
public class ShippingAddress {

    @Setter
    @Column(nullable = false)
    private String flatNoOrDoorNo;

    @Setter
    @Column(nullable = false)
    private String street;

    @Setter
    @Column(nullable = false)
    private String area;

    @Setter
    @Column(nullable = false)
    private String city;

    @Setter
    @Column(nullable = false)
    private String state;

    @Setter
    @Column(nullable = false)
    private String pinCode;

}
