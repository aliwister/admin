package com.badals.admin.repository;
import com.badals.admin.domain.OrderShipment;
import com.badals.admin.domain.PurchaseShipment;
import com.badals.admin.domain.ShipmentItem;
import com.fasterxml.jackson.core.TreeNode;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the OrderShipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderShipmentRepository extends JpaRepository<OrderShipment, Long> {

    Optional<OrderShipment> findByShipmentItem(ShipmentItem shipmentItem);
    void deleteByShipmentItem(ShipmentItem shipmentItem);
}
