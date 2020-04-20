package com.badals.admin.repository;
import com.badals.admin.domain.Shipment;
import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.projection.Inventory;
import com.badals.admin.domain.projection.OutstandingQueue;
import com.badals.admin.domain.projection.SortQueue;
import com.badals.admin.service.dto.ShipmentDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Shipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Query("from Shipment s where s.customer.id = ?1 and s.shipmentStatus = ?2")
    Long findCustomerShipment(Long customerId, ShipmentStatus s);

    @Query(value="Select s.id from shipment s join shop.jhi_order o on s.reference = o.reference and s.shipment_status = 'PENDING' and s.shipment_type='CUSTOMER' where o.id = :id", nativeQuery=true)
    Long findCustomerShipmentIdNative(@Param(value = "id") Long orderId);

    @Query("from Shipment s where s.shipmentStatus in ?1 and s.shipmentType = ?2")
    List<Shipment> findForShipmentList(List<ShipmentStatus> status, ShipmentType type);

    @Query(value="select * from sort_queue where description like %:keyword% ", nativeQuery=true)
    List<SortQueue> findForSorting(@Param(value = "keyword") String keyword);

    @Query(value="select * from outstanding_queue where description like %:keyword% ", nativeQuery=true)
    List<OutstandingQueue> findOutstanding(@Param(value = "keyword") String keyword);

    @Query(value="select * from outstanding_queue ", nativeQuery=true)
    List<OutstandingQueue> findOutstanding();

    @Query(value="SELECT product_id AS productId, received, issued, received-issued AS quantityOnHand, title, image, sku FROM inventory", nativeQuery = true)
    List<Inventory> getInventory();
}
