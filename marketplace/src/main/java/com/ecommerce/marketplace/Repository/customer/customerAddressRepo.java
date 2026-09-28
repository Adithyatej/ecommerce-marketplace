package com.ecommerce.marketplace.Repository.customer;

import com.ecommerce.marketplace.entities.customer.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface customerAddressRepo extends JpaRepository<Address,Long> {
}
