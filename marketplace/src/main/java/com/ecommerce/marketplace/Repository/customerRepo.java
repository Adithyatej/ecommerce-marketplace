package com.ecommerce.marketplace.Repository;

import com.ecommerce.marketplace.entities.customer.customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface customerRepo extends JpaRepository<customer, Long> {


    @Query(value = "SELECT * FROM customer WHERE email = ?1 or phone = ?2", nativeQuery = true)
    public customer findByEmailOrPhone(String email, String phone);
}
