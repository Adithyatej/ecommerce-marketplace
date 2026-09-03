package com.ecommerce.marketplace.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor

@Getter
@Setter
public class sellerAddressDTO {

    private String doorNumber;

    private String street;

    private String city;

    private String state;

    private Long pinCode;
}
