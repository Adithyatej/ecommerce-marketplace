package com.ecommerce.marketplace.Repository.product;

import com.ecommerce.marketplace.entities.product.products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface productRepo extends JpaRepository<products,Long> {
}
