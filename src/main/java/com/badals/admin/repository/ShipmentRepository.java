package com.badals.admin.repository;
import com.badals.admin.domain.Shipment;
import com.badals.admin.domain.ShipmentItem;
import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.projection.ShipmentItemCount;
import com.badals.admin.domain.projection.*;
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

    @Query("from Shipment s left join fetch s.merchant left join fetch s.customer where s.shipmentStatus in ?1 and s.shipmentType = ?2")
    List<Shipment> findForShipmentList(List<ShipmentStatus> status, ShipmentType type);

    @Query(value="select * from sort_queue where description like %:keyword% ", nativeQuery=true)
    List<SortQueue> findForSorting(@Param(value = "keyword") String keyword);

    @Query(value="SELECT * FROM (select p.id AS id,p.purchase_id AS po,p.description AS description,p.shipping_instructions AS shippingInstructions,p.quantity AS quantity,p.price AS cost,p.product_id AS productId,sum(ifnull((sr.accepted + sr.rejected),0)) AS preallocated,(select o.sku from (shop.order_item o join shop.purchase_item_order_item pioi on((o.id = pioi.order_item_id))) where (pioi.purchase_item_id = p.id)) AS sku,(select o.price from (shop.order_item o join shop.purchase_item_order_item pioi on((o.id = pioi.order_item_id))) where (pioi.purchase_item_id = p.id)) AS price,(select o.url from (shop.order_item o join shop.purchase_item_order_item pioi on((o.id = pioi.order_item_id))) where (pioi.purchase_item_id = p.id)) AS url,(select o.weight from (shop.order_item o join shop.purchase_item_order_item pioi on((o.id = pioi.order_item_id))) where (pioi.purchase_item_id = p.id)) AS weight,(select o.image from (shop.order_item o join shop.purchase_item_order_item pioi on((o.id = pioi.order_item_id))) where (pioi.purchase_item_id = p.id)) AS image,(select o.id from (shop.order_item o join shop.purchase_item_order_item pioi on((o.id = pioi.order_item_id))) where (pioi.purchase_item_id = p.id)) AS orderItemId,(select o.order_id from (shop.order_item o join shop.purchase_item_order_item pioi on((o.id = pioi.order_item_id))) where (pioi.purchase_item_id = p.id)) AS orderId,(select pp.merchant_id from shop.purchase pp where (pp.id = p.purchase_id)) AS merchantId from ((((((shop.purchase_item p join shop.purchase_item_order_item pioi on((p.id = pioi.purchase_item_id))) join shop.order_item o on((o.id = pioi.order_item_id))) left join purchase_shipment ps on((p.id = ps.purchase_item_id))) left join shipment_item si on((ps.shipment_item_id = si.id))) left join shipment s on(((s.id = si.id) and (s.shipment_type = 'PURCHASE')))) left join shipment_receipt sr on((si.id = sr.shipment_item_id))) where purchase_id = :po group by p.id having (p.quantity > preallocated)) AS x WHERE sku = :sku ", nativeQuery=true)
    List<SortQueue> findForTransit(@Param(value = "po") String po, @Param(value = "sku") String sku);


    @Query(value="select * from outstanding_queue where description like %:keyword% ", nativeQuery=true)
    List<OutstandingQueue> findOutstanding(@Param(value = "keyword") String keyword);

    @Query(value="select * from outstanding_queue ", nativeQuery=true)
    List<OutstandingQueue> findOutstanding();

    @Query(value="SELECT product_id AS productId, received, issued, received-issued AS quantityOnHand, title, image, sku FROM inventory", nativeQuery = true)
    List<Inventory> getInventory();

    @Query(value="SELECT * FROM ship_queue", nativeQuery = true)
    List<ShipQueue> getShipQueue();

    @Query(value="SELECT si.id, si.sequence, si.quantity, si.description, si.shipment_id as shipmentId, si.product_id as productId, si.quantity - ifnull(sum(pc.quantity),0) as unpacked, ifnull(p.image,'') as image " +
        ",  (SELECT o.id from  shop.order_item o join shop.purchase_item_order_item pioi on o.id = pioi.order_item_id where pioi.purchase_item_id = ps.purchase_item_Id) AS orderItemId " +
        ",  (SELECT o.order_id from  shop.order_item o join shop.purchase_item_order_item pioi on o.id = pioi.order_item_id where pioi.purchase_item_id = ps.purchase_item_Id) AS orderId " +
        "FROM shipment_item si " +
        "LEFT JOIN packaging_content pc ON si.id = pc.shipment_item_id " +
        "LEFT JOIN shop.product p ON si.product_id = p.ref " +
        "LEFT JOIN purchase_shipment ps ON ps.shipment_item_id = si.id " +
        "WHERE si.shipment_id = :shipmentId and si.description like %:keyword% " +
        "GROUP BY si.id " +
        "HAVING unpacked > 0", nativeQuery = true)
    List<PrepQueue> getPrepQueue(@Param(value = "shipmentId") Long shipmentId , @Param(value = "keyword") String keyword);

    @Query
    List<Shipment> findAllByReference(String reference);

    @Query("from Shipment s left join fetch s.shipmentItems where s.trackingNum = ?1 ")
    Optional<Shipment> findByTrackingNum(String trackingNum);

    @Query("from ShipmentItem si join si.shipment s join fetch si.product where s.trackingNum in ?1")
    List<ShipmentItem> findByTrackingNums(List<String> trackingNums);

    @Query("select s.trackingNum as trackingNum, count(si) as count from Shipment s left join s.shipmentItems si where s.trackingNum in ?1 group by s.trackingNum")
    List<ShipmentItemCount> findCountByTrackingNums(List<String> trackingNums);
}
