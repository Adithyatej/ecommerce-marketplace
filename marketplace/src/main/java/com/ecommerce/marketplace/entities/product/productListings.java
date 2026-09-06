package com.ecommerce.marketplace.entities.product;


import com.ecommerce.marketplace.entities.seller.seller;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "product_listings")
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
    private seller seller;

    @Setter
    @Column(name = "price", nullable = false)
    private Double price;

    @Setter
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Setter
    @Column(name = "status",nullable = false)
    private String status;


}
