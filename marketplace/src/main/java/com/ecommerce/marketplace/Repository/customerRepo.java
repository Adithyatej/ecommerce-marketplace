package com.ecommerce.marketplace.Repository;

import com.ecommerce.marketplace.entities.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface customerRepo extends JpaRepository<Customer, Long> {


    @Query(value = "SELECT * FROM customer WHERE email = ?1 or phone = ?2", nativeQuery = true)
    public Customer findByEmailOrPhone(String email, String phone);

    @Query(value = "SELECT * FROM CUSTOMER WHERE EMAIL=:email",nativeQuery = true)
    Customer findByEmail(@Param("email") String email);
}
