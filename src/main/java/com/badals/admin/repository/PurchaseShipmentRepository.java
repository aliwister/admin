package com.badals.admin.repository;
import com.badals.admin.domain.ItemIssuance;
import com.badals.admin.domain.PurchaseShipment;
import com.badals.admin.domain.ShipmentItem;
import com.fasterxml.jackson.core.TreeNode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the PurchaseShipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseShipmentRepository extends JpaRepository<PurchaseShipment, Long> {

    Optional<PurchaseShipment> findByShipmentItem(ShipmentItem shipmentItem);

    void deleteByShipmentItem(ShipmentItem shipmentItem);
}
