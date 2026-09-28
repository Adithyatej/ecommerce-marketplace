package com.ecommerce.marketplace.Mapping;


import com.ecommerce.marketplace.entities.customer.Address;
import com.ecommerce.marketplace.entities.orders.ShippingAddress;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    ShippingAddress toShippingAddress(Address address);
}
