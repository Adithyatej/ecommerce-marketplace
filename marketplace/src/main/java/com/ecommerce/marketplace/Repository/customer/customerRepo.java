package com.ecommerce.marketplace.Repository.customer;

import com.ecommerce.marketplace.entities.customer.Address;
import com.ecommerce.marketplace.entities.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface customerRepo extends JpaRepository<Customer, Long> {


    @Query(value = "SELECT * FROM customer WHERE email = ?1 or phone = ?2", nativeQuery = true)
    public Customer findByEmailOrPhone(String email, String phone);

    @Query(value = "SELECT * FROM CUSTOMER WHERE EMAIL=:email",nativeQuery = true)
    Customer findByEmail(@Param("email") String email);

    @Query(value = "SELECT A.* FROM ADDRESSES A JOIN CUSTOMER C ON A.CUSTOMER_ID=C.ID WHERE C.EMAIL=?1 AND A.ID=?2",nativeQuery = true)
    Optional<Address> findAddressByEmail(String email, Long AddressId);
}
