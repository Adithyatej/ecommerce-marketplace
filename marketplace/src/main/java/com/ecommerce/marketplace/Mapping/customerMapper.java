package com.ecommerce.marketplace.Mapping;

import com.ecommerce.marketplace.dto.customer.customerAddressDTO;
import com.ecommerce.marketplace.dto.customer.customerRequestDTO;
import com.ecommerce.marketplace.entities.customer.Address;
import com.ecommerce.marketplace.entities.customer.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface customerMapper {

    @Mapping(source = "name",target = "username")
    Customer dtoToEntity(customerRequestDTO customerDto);

    Address DtoToAddressEntity(customerAddressDTO customerAddress, Customer customer);
}
