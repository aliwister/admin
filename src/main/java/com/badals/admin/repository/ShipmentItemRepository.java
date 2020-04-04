package com.badals.admin.repository;
import com.badals.admin.domain.ShipmentItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShipmentItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentItemRepository extends JpaRepository<ShipmentItem, Long> {

}
