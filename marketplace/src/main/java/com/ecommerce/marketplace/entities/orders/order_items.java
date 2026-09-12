package com.ecommerce.marketplace.entities.orders;


import com.ecommerce.marketplace.entities.product.productListings;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "order_items")
public class order_items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productListing_id",referencedColumnName = "id")
    private productListings productListing;

    @Setter
    @Column(name = "order_quantity")
    private Integer quantity;

    @Setter
    @Column(name = "unit price")
    private BigDecimal unitPrice;



}
