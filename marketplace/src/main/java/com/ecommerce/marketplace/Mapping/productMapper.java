package com.ecommerce.marketplace.Mapping;


import com.ecommerce.marketplace.dto.product.productDTO;
import com.ecommerce.marketplace.entities.product.productCategories;
import com.ecommerce.marketplace.entities.product.products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface productMapper {


    @Mapping(target = "productCategories",source = "category")
    products toEntity(productDTO product, productCategories category);

    @Mapping(target = "productCategory",source = "productCategories.categoryName")
    productDTO toDTO(products product);
}
