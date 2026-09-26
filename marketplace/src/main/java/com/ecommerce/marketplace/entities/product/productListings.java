package com.ecommerce.marketplace.entities.product;


import com.ecommerce.marketplace.entities.seller.Seller;
import com.ecommerce.marketplace.enums.ProductListingPolicy;
import com.ecommerce.marketplace.enums.ProductListingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "product_listings",
uniqueConstraints = @UniqueConstraint(name = "unique_product_listing",
columnNames = {"product_id","seller_id"}))

public class productListings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private products product;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id",referencedColumnName ="id")
    private Seller seller;

    @Setter
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Setter
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Setter
    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductListingStatus status;

    @Setter
    @Column(name="policy", nullable = false)
    @Enumerated
    public ProductListingPolicy policy;


}
