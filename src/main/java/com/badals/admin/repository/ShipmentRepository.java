package com.badals.admin.repository;
import com.badals.admin.domain.Shipment;
import com.badals.admin.domain.ShipmentItem;
import com.badals.admin.domain.ShipmentTracking;
import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.projection.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Query("from ShipmentItem si join si.shipment s join fetch si.product where s.trackingNum in ?1 and s.shipmentStatus not in ?2")
    List<ShipmentItem> findByTrackingNums(List<String> trackingNums, List<ShipmentStatus> exclude);


    @Query
    List<Shipment> findAllByTrackingNumIn(List<String> trackingNums);

    //@Query("select s.trackingNum as trackingNum, s.shipmentStatus as status, count(si) as count, count(si2) as processed from Shipment s left join s.shipmentItems si left join s.shipmentItems si2 where si2.from" +
    //    " = si.id and s.trackingNum in ?1 group by s.trackingNum")

    @Query(value="select s.tracking_num as trackingNum, s.shipment_status as status, " +
        "count(si.id) as total, " +
        "count(si2.id) as processed " +
        "from shipment s left join shipment_item si ON s.id = si.shipment_id " +
        "left join shipment_item si2 ON si2._from = si.id where s.tracking_num in :trackingNums " +
        "group by trackingNum", nativeQuery = true)
    List<ShipmentItemSummary> findCountByTrackingNums(@Param(value = "trackingNums")  List<String> trackingNums);

    @Modifying(clearAutomatically=true)
    @Transactional
    @Query("update Shipment s set s.shipmentStatus = ?1 where s.trackingNum in ?2")
    void setStatusMulti(ShipmentStatus status, List<String> trackingNums);

    @Query(value="SELECT si.description, oi.image, si.quantity, s.id, s.shipment_type AS type, s.shipment_status AS status, s.last_modified_date AS date, s.tracking_num as trackingNum, s.shipment_method as carrier FROM shipment s " +
        "JOIN shipment_item si ON s.id = si.shipment_id " +
        "JOIN purchase_shipment ps ON ps.shipment_item_id = si.id " +
        "JOIN shop.purchase_item_order_item pioi ON pioi.purchase_item_id = ps.purchase_item_id " +
        "JOIN shop.order_item oi ON pioi.order_item_id = oi.id " +
        "JOIN shop.jhi_order o ON oi.order_id = o.id " +
        "WHERE o.reference = :ref and s.shipment_status != 'CLOSED' AND NOT EXISTS ( " +
        " SELECT 1  " +
        " FROM order_shipment os " +
        " WHERE os.order_item_id = oi.id " +
        ")  " +
        "UNION  " +
        "SELECT si.description, oi.image, si.quantity, s.id, s.shipment_type AS type, s.shipment_status AS status, s.last_modified_date AS date, s.tracking_num as trackingNum, s.shipment_method as carrier FROM shipment s " +
        "JOIN shipment_item si ON s.id = si.shipment_id " +
        "JOIN order_shipment os ON si.id = os.shipment_item_id " +
        "JOIN shop.order_item oi ON oi.id = os.order_item_id " +
        "WHERE s.reference = :ref  ", nativeQuery = true)
    List<Tracking> trackByRef(@Param("ref") String ref);

    @Query("from ShipmentTracking ste join fetch ste.shipmentEvent join fetch ste.shipment s where s.id in ?1")
    List<ShipmentTracking> trackingProgress(Set<Long> shipmentIds);
}
