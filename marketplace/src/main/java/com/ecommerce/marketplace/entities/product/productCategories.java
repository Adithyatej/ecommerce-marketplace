package com.ecommerce.marketplace.entities.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name="product_categories")
public class productCategories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Setter
    @Column(name = "category_description", nullable = false)
    private String categoryDescription;

    @Setter
    @Column(name = "parent_category_id")
    private Long parentCategoryId;

    @Setter
    @Column(name = "tags", nullable = false)
    private List<String> tags;

    @Setter
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<products> products;




}
