package com.ecommerce.marketplace.Mapping;


import com.ecommerce.marketplace.dto.seller.sellerRequestDTO;
import com.ecommerce.marketplace.entities.seller.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface sellerMapper {

    @Mapping(source = "name",target = "username")
    @Mapping(source = "sellerAddress",target = "address")
    Seller toEntity(sellerRequestDTO sellerDTO);


}
