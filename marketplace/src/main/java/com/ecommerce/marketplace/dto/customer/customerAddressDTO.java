package com.ecommerce.marketplace.dto.customer;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class customerAddressDTO {

    private String email;

    private String flatNoOrDoorNo;

    private String street;


    private String area;


    private String city;


    private String state;


    private String pinCode;

}
