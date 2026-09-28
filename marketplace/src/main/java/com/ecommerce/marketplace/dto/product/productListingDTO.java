package com.ecommerce.marketplace.dto.product;


import com.ecommerce.marketplace.enums.ProductListingPolicy;
import com.ecommerce.marketplace.enums.ProductListingStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class productListingDTO {


    @NotBlank(message = "product name is mandatory")
    private String productName;

    @NotBlank(message = "brand is mandatory")
    private String brand;

    @NotBlank(message = "seller mail is mandatory")
    private String seller;

    @Min(value = 1, message = "pricing  should least be 1")
    private BigDecimal price;

    @Min(value = 1,message = "stock quantity is mandatory")
    private Integer stockQuantity;

    @NotNull(message = "status is mandatory")
    private ProductListingStatus status;

    @NotNull(message = "policy is mandatory")
    public ProductListingPolicy policy;



}
