package com.ecommerce.marketplace.service.product;


import com.ecommerce.marketplace.Repository.product.productCategoriesRepo;
import com.ecommerce.marketplace.dto.product.categoryDTO;
import com.ecommerce.marketplace.entities.product.productCategories;
import com.ecommerce.marketplace.exceptions.IdNotFoundException;
import com.ecommerce.marketplace.exceptions.categoryNotFoundException;
import jakarta.transaction.Transactional;
import jdk.jfr.Category;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class productServices {

    private final productCategoriesRepo productCategoriesRepo;

    public productServices(productCategoriesRepo productCategoriesRepo){
        this.productCategoriesRepo=productCategoriesRepo;
    }

    public void add(categoryDTO request) {

        productCategories info = productCategoriesRepo.findByCategoryName(request.getParentCategoryName()).orElseThrow(
                ()-> new IdNotFoundException("Category Not Found")
        );

            productCategories productCategories= new productCategories();
            productCategories.setCategoryName(request.getCategoryName());
            productCategories.setParentCategoryId(info);
            productCategoriesRepo.save(productCategories);


    }

    public List<categoryDTO> getCategoriesAndTheirParentCategories() {

       List<categoryDTO> category = productCategoriesRepo.findCategoriesAndSubCategories();

       return  (category.isEmpty())?List.of(): category.stream().toList();
    }


    public List<String> getCategories() {

        List<String> category = productCategoriesRepo.findAllCategories();

         if (category==null || category.isEmpty()) {
             throw new categoryNotFoundException("Categories not found");
         }

         else {
             return category;
         }
    }

    @Transactional(rollbackOn = Exception.class)
    public Long deleteCategories(String name) {

        name = name.replace("-"," ");

        Long rows = productCategoriesRepo.deleteByCategoryName(name);

        if (rows<1) {
            throw new categoryNotFoundException("Category Not Found");
        }

        return rows;

    }
}
