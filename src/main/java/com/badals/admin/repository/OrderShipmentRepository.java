package com.badals.admin.repository;
import com.badals.admin.domain.OrderShipment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrderShipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderShipmentRepository extends JpaRepository<OrderShipment, Long> {

}
