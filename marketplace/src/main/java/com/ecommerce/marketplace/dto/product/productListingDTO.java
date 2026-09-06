package com.ecommerce.marketplace.dto.product;


import com.ecommerce.marketplace.entities.product.products;
import com.ecommerce.marketplace.entities.seller.seller;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

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
    private Double price;

    @Min(value = 1,message = "stock quantity is mandatory")
    private Integer stockQuantity;

    @NotBlank(message = "status is mandatory")
    private String status;


}
