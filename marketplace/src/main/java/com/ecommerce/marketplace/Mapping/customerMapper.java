package com.ecommerce.marketplace.Mapping;

import com.ecommerce.marketplace.dto.customerRequestDTO;
import com.ecommerce.marketplace.entities.customer.customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface customerMapper {

    @Mapping(source = "name",target = "username")
    customer dtoToEntity(customerRequestDTO customerDto);

}
