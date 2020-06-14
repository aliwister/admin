package com.badals.admin.repository;

import com.badals.admin.domain.PurchaseItem;
import com.badals.admin.domain.projection.SortQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PurchaseItem entity.
 */
@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

}
