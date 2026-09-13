package com.ecommerce.marketplace.Mapping;

import com.ecommerce.marketplace.entities.customer.Customer;
import com.ecommerce.marketplace.entities.orders.Order;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toEntity(Customer customer, BigDecimal totalAmount,);
}
