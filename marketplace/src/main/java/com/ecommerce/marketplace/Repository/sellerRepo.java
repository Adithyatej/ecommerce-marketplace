package com.ecommerce.marketplace.Repository;

import com.ecommerce.marketplace.entities.seller.seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface sellerRepo extends JpaRepository<seller,Long> {
}
