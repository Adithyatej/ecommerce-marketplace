package com.ecommerce.marketplace.Repository.customer;

import com.ecommerce.marketplace.entities.customer.Securitytoken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface refreshTokenRepo extends JpaRepository<Securitytoken,Long> {

    @Query(value = "SELECT * FROM SECURITY_TOKENS WHERE TOKEN=?1")
    Securitytoken findByToken(String token);
}
