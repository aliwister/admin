package com.badals.admin.repository;
import com.badals.admin.domain.PackagingContent;
import com.badals.admin.domain.ShipmentItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the PackagingContent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PackagingContentRepository extends JpaRepository<PackagingContent, Long> {

   Optional<PackagingContent> findByShipmentItem(ShipmentItem item);
   void deleteByShipmentItem(ShipmentItem item);
}
