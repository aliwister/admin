package com.badals.admin.repository;

import com.badals.admin.domain.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PurchaseItem entity.
 */
@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {


}
