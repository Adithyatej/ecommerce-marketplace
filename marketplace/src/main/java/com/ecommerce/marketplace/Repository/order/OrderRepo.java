package com.ecommerce.marketplace.Repository.order;

import com.ecommerce.marketplace.entities.orders.Orders;
import jakarta.persistence.criteria.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Orders, Long> {
}
