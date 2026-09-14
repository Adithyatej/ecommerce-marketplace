package com.ecommerce.marketplace.dto.OrderSystem;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDTO {

    private String email;

    private Long listingId;

    private Integer quantity;

    private Long ShippingAddress;
}
