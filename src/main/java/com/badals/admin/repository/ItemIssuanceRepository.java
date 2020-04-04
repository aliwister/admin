package com.badals.admin.repository;
import com.badals.admin.domain.ItemIssuance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ItemIssuance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemIssuanceRepository extends JpaRepository<ItemIssuance, Long> {

}
