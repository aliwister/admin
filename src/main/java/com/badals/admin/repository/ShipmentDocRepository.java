package com.badals.admin.repository;
import com.badals.admin.domain.ShipmentDoc;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShipmentDoc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentDocRepository extends JpaRepository<ShipmentDoc, Long> {

}
