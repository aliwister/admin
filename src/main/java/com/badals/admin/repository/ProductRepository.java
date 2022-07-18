package com.badals.admin.repository;

import com.badals.admin.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the  ShipmentTracking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

   @Query(value="select p.`ref` from profileshop.product p where sku = :asin", nativeQuery=true)
   Optional<Long> getRefByKey(@Param(value = "asin") String asin);

}
