package com.ecommerce.marketplace.entities.orders;


import com.ecommerce.marketplace.entities.product.productListings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "order_items")
public class orderItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productListing_id",referencedColumnName = "id")
    private productListings productListing;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_order", referencedColumnName = "id", nullable = false)
    private sellerOrderBoard sellerOrder;

    @Setter
    @Column(name = "order_quantity")
    private Integer quantity;

    @Setter
    @Column(name = "unit_price",nullable = false,precision = 12,scale = 2)
    private BigDecimal unitPrice;



}
