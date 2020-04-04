package com.badals.admin.repository;
import com.badals.admin.domain.ShipmentReceipt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShipmentReceipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentReceiptRepository extends JpaRepository<ShipmentReceipt, Long> {

}
