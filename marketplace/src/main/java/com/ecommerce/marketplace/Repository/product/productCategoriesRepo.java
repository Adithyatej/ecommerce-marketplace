package com.ecommerce.marketplace.Repository.product;

import com.ecommerce.marketplace.dto.product.categoryDTO;
import com.ecommerce.marketplace.entities.product.productCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface productCategoriesRepo extends JpaRepository<productCategories,Long> {

    Optional<productCategories> findByCategoryName(String parentCategoryName);

    @Query(value = "select ep.category_name, epc.category_name from product_categories ep join product_categories epc on ep.parent_category_id=epc.id",nativeQuery = true, countQuery = "select count(*) from product_categories")
    List<categoryDTO> findCategoriesAndSubCategories();

    @Query(value = "select category_name from product_categories",nativeQuery = true)
    List<String> findAllCategories();


    Long deleteByCategoryName(String categoryName);
}
