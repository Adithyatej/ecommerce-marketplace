package com.ecommerce.marketplace.Repository.product;

import com.ecommerce.marketplace.entities.product.productListings;
import com.ecommerce.marketplace.projections.products.productListingResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface productListingRepo extends JpaRepository<productListings,Long> {


    @Query(value = "SELECT PL.*,P.PRODUCT_NAME,S.USERNAME FROM PRODUCT_LISTINGS PL JOIN PRODUCTS P ON PL.PRODUCT_ID= P.ID JOIN SELLERS S ON PL.SELLER_ID = S.ID WHERE PL.PRODUCT_ID = ?1 AND PL.SELLER_ID = ?2",nativeQuery = true)
    List<productListings> findProductList(Long id, Long id1);


    @Query(value = "SELECT PL.PRICE AS price,PL.STOCK_QUANTITY as stockQuantity, P.PRODUCT_NAME AS productName,P.BRAND AS brand, S.USERNAME AS  sellerName FROM PRODUCT_LISTINGS PL JOIN PRODUCTS P ON PL.PRODUCT_ID= P.ID JOIN SELLERS S ON PL.SELLER_ID = S.ID WHERE S.EMAIL=?1",nativeQuery = true)
    List<productListingResponse> findProductListingBySeller(@Param("seller") String seller);

    @Query(value = "SELECT PL.PRICE AS price,PL.STOCK_QUANTITY as stockQuantity, P.PRODUCT_NAME AS productName,P.BRAND AS brand, S.USERNAME AS  sellerName FROM PRODUCT_LISTINGS PL JOIN PRODUCTS P ON PL.PRODUCT_ID= P.ID JOIN SELLERS S ON PL.SELLER_ID = S.ID WHERE S.EMAIL=?1 AND P.PRODUCT_NAME=?2 AND P.BRAND=?3",nativeQuery = true)
    List<productListingResponse> findProductListingByPRODUCT(@Param("seller") String seller, @Param("productName") String productName, @Param("brand") String brand);

    @Modifying
    @Query(value = "UPDATE PRODUCT_LISTINGS AS PL INNER JOIN PRODUCTS AS P ON PL.PRODUCT_ID=P.ID INNER JOIN SELLERS AS S ON PL.SELLER_ID=S.ID SET PL.STOCK_QUANTITY=?4 WHERE S.EMAIL=?1 AND P.PRODUCT_NAME=?2 AND P.BRAND=?3",nativeQuery = true)
    Integer updateStockQuantityByProduct(String seller, String productName, String brand,Integer stockQuantity);

    @Modifying
    @Query(value = "UPDATE PRODUCT_LISTINGS AS PL INNER JOIN PRODUCTS AS P ON PL.PRODUCT_ID=P.ID INNER JOIN SELLERS AS S ON PL.SELLER_ID=S.ID SET PL.PRICE=?4 WHERE S.EMAIL=?1 AND P.PRODUCT_NAME=?2 AND P.BRAND=?3",nativeQuery = true)
    Integer updatePriceByProduct(String seller, String productName, String brand, BigDecimal price);

    @Modifying
    @Query(value = "UPDATE PRODUCT_LISTINGS AS PL INNER JOIN PRODUCTS AS P ON PL.PRODUCT_ID=P.ID INNER JOIN SELLERS AS S ON PL.SELLER_ID=S.ID SET PL.STATUS=?4 WHERE S.EMAIL=?1 AND P.PRODUCT_NAME=?2 AND P.BRAND=?3",nativeQuery = true)
    Integer updateStatusByProduct(String seller, String productName, String brand, String status);

    @Modifying
    @Query(value = "UPDATE PRODUCT_LISTINGS AS PL INNER JOIN PRODUCTS AS P ON PL.PRODUCT_ID=P.ID INNER JOIN SELLERS AS S ON PL.SELLER_ID=S.ID SET PL.STATUS= 'DEACTIVATED' WHERE S.EMAIL=?1 AND P.PRODUCT_NAME=?2 AND P.BRAND=?3",nativeQuery = true)
    Integer deleteProductListing(String seller,String productName, String brand);


    @Query(value = "SELECT PL.PRICE AS price,PL.STOCK_QUANTITY as stockQuantity, P.PRODUCT_NAME AS productName,P.BRAND AS brand, S.USERNAME AS sellerName FROM PRODUCT_LISTINGS PL JOIN PRODUCTS P ON PL.PRODUCT_ID= P.ID JOIN SELLERS S ON PL.SELLER_ID = S.ID",nativeQuery = true)
    List<productListingResponse> findAllProductListing();

    @Query(value = "SELECT * FROM PRODUCT_LISTINGS WHERE ID=?1 AND STATUS NOT IN ('OUT OF STOCK','DEACTIVATED')",nativeQuery = true)
    Optional<productListings> findByIdAndStatus(Long listingId);
}

