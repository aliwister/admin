package com.badals.admin.repository;
import com.badals.admin.domain.Shipment;
import com.badals.admin.domain.ShipmentDoc;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 * Spring Data  repository for the ShipmentDoc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentDocRepository extends JpaRepository<ShipmentDoc, Long> {

    @Query("from ShipmentDoc d where d.shipment.id in ?1")
    List<ShipmentDoc> findByShipmentIdIn(Set<Long> shipmentIds);

   List<ShipmentDoc> findByShipment(Shipment s);
}
