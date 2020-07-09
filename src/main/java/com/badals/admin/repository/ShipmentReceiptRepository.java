package com.badals.admin.repository;
import com.badals.admin.domain.ShipmentItem;
import com.badals.admin.domain.ShipmentReceipt;
import com.fasterxml.jackson.core.TreeNode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the ShipmentReceipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentReceiptRepository extends JpaRepository<ShipmentReceipt, Long> {

   Optional<ShipmentReceipt> findByShipmentItem(ShipmentItem shipmentItem);
   void deleteByShipmentItem(ShipmentItem shipmentItem);
}
