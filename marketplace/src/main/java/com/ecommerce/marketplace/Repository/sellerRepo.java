package com.ecommerce.marketplace.Repository;

import com.ecommerce.marketplace.entities.customer.customer;
import com.ecommerce.marketplace.entities.seller.seller;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface sellerRepo extends JpaRepository<seller,Long> {

    @Query(value = "SELECT * FROM SELLERS WHERE email = ?1 or phone = ?2", nativeQuery = true)
    public seller findByEmailOrPhone(String email, String phone);


    seller findByEmail(String seller);
}
