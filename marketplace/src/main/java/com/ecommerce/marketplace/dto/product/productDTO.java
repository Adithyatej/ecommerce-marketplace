package com.ecommerce.marketplace.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
public class productDTO {

    @Setter
    private String productName;

    @Setter
    private String productDescription;

    @Setter
    private String brand;

    @Setter
    private String productCategory;

}
