package com.ecommerce.marketplace.Repository;

import com.ecommerce.marketplace.entities.seller.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface sellerRepo extends JpaRepository<Seller,Long> {

    @Query(value = "SELECT * FROM SELLERS WHERE email = ?1 or phone = ?2", nativeQuery = true)
    public Seller findByEmailOrPhone(String email, String phone);


    Seller findByEmail(String seller);
}
