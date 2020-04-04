package com.badals.admin.repository;
import com.badals.admin.domain.PurchaseShipment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PurchaseShipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseShipmentRepository extends JpaRepository<PurchaseShipment, Long> {

}
