package com.ecommerce.marketplace.dto.cartSystem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class cartRequestDTO {

    private String email;

    private Long listingId;

    private Integer quantity;
}
