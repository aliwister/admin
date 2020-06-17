package com.badals.admin.repository;
import com.badals.admin.domain.ShipmentEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShipmentEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentEventRepository extends JpaRepository<ShipmentEvent, Long> {

}
