package com.badals.admin.repository;
import com.badals.admin.domain.Shipment;
import com.badals.admin.domain.ShipmentItem;
import com.badals.admin.domain.ShipmentTracking;
import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.pojo.ItemTrackingPojo;
import com.badals.admin.domain.pojo.PaymentPojo;
import com.badals.admin.domain.projection.*;
import com.badals.admin.service.dto.ShipmentDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Query(value="Select s.id from shipment s join shop.jhi_order o on s.reference = o.reference and s.shipment_status = 'ARRIVED' and s.shipment_type='CUSTOMER' where o.id = :id", nativeQuery=true)
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


    @Query(value="SELECT * FROM ship_queue where customerId = :cutomerId", nativeQuery = true)
    List<ShipQueue> getShipQueueByCustomerId(@Param(value = "cutomerId") Long cutomerId);

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

    @Query("from ShipmentItem si join si.shipment s left join fetch si.product where s.trackingNum in ?1 and s.shipmentStatus not in ?2")
    List<ShipmentItem> findByTrackingNumsExclude(List<String> trackingNums, List<ShipmentStatus> exclude);

    @Query("from ShipmentItem si join si.shipment s left join fetch si.product where s.trackingNum in ?1")
    List<ShipmentItem> findByTrackingNums(List<String> trackingNums);


    @Query
    List<Shipment> findAllByTrackingNumIn(List<String> trackingNums);

    //@Query("select s.trackingNum as trackingNum, s.shipmentStatus as status, count(si) as count, count(si2) as processed from Shipment s left join s.shipmentItems si left join s.shipmentItems si2 where si2.from" +
    //    " = si.id and s.trackingNum in ?1 group by s.trackingNum")

    @Query(value="select s.id as id, s.tracking_num as trackingNum, s.shipment_status as status, s.reference as reference, " +
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

    @Query(value="SELECT si.id as shipmentItemId, si.description, oi.image, si.quantity, s.id, s.shipment_type AS type, s.shipment_status AS status, s.last_modified_date AS date, s.tracking_num as trackingNum, s.shipment_method as carrier FROM shipment s " +
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
        "SELECT si.id as shipmentItemId, si.description, oi.image, si.quantity, s.id, s.shipment_type AS type, s.shipment_status AS status, s.last_modified_date AS date, s.tracking_num as trackingNum, s.shipment_method as carrier FROM shipment s " +
        "JOIN shipment_item si ON s.id = si.shipment_id " +
        "JOIN order_shipment os ON si.id = os.shipment_item_id " +
        "JOIN shop.order_item oi ON oi.id = os.order_item_id " +
        "WHERE s.reference = :ref  ", nativeQuery = true)
    List<Tracking> trackByRef(@Param("ref") String ref);

    @Query("from ShipmentTracking ste join fetch ste.shipmentEvent join fetch ste.shipment s where s.id in ?1")
    List<ShipmentTracking> trackingProgress(Set<Long> shipmentIds);

    @Query(value="select * from incoming_shipment_queue", nativeQuery = true)
    List<ShipmentList> unclosedPurchase();

    @Modifying @Query(value="insert into shop.payment(payment_method, amount, customer_id, ref, invoice_num , account) values ('CASH', :amount, :user_id, :ref, :invoiceNum, :account) ON DUPLICATE KEY UPDATE amount = :amount", nativeQuery = true)
    void addPayment(@Param("user_id") Long userId, @Param("ref") String ref, @Param("invoiceNum") String invoiceNum, @Param("account") String account, @Param("amount") BigDecimal amount);

    @Query(value="select s.id AS id,s.created_date AS createdDate,s.shipment_method AS shipmentMethod,s.tracking_num AS trackingNum,s.pkg_count AS pkgCount,count(p.id) AS arrivedPkgs,s.shipment_status AS STATUS, " +
        "(SELECT sum(si.quantity) from shipment_item si where s.id = si.shipment_id) AS sent, " +
        "(select SUM(sr.accepted + sr.rejected) FROM shipment_receipt sr join shipment_item si ON sr.shipment_item_id= si.id WHERE s.id = si.shipment_id) AS received, " +
        "(select m2.name from shop.merchant m2 where s.merchant_id = m2.id) AS sender " +
        "from  shipment s " +
        "left join pkg p on p.shipment_id = s.id  " +
        "where s.shipment_type = 'PURCHASE' AND s.shipment_status != 'CLOSED'" +
        "group by s.id having  s.pkg_count > arrivedPkgs OR sent != received order by id desc", nativeQuery = true)
    List<ShipmentList> incomingShipments();

    @Query(value="select s.id AS id,s.created_date AS createdDate,s.shipment_method AS shipmentMethod,s.tracking_num AS trackingNum, s.shipment_status AS status from shipment s where s.shipment_type = :shipmentType AND s.shipment_status <> :shipmentStatus", nativeQuery = true)
    List<ShipmentList> shipQByTypeAndStatusNot(@Param("shipmentType") String shipmentType, @Param("shipmentStatus") String shipmentStatus);

    @Query(value="select s.id AS id,s.created_date AS createdDate,s.shipment_method AS shipmentMethod,s.tracking_num AS trackingNum, s.shipment_status AS status from shipment s where s.shipment_type = :shipmentType AND s.shipment_status = :shipmentStatus", nativeQuery = true)
    List<ShipmentList> shipQByTypeAndStatus(@Param("shipmentType") String shipmentType, @Param("shipmentStatus") String shipmentStatus);

    @Query(value="select * from unshipped_queue ", nativeQuery=true)
    List<UnshippedQueue> findUnshipped();

    @Query(value="select count(1) + 1 as sequence from shipment s where s.id = :id",nativeQuery = true)
    Integer nextSeq(@Param("id") Long id);


    @Query(value="select  " +
        "          si.id, " +
        "          si.sequence, " +
        "          si.quantity, " +
        "          si.description, " +
        "          si.shipment_id as shipmentId, " +
        "          si.product_id as productId, " +
        "          p.image as image " +
        "from shipment_item si left join shop.product p on p.ref = si.product_id  " +
        "where si.shipment_id = :id", nativeQuery = true)
    List<ShipmentItemDetails> findItemsForShipmentDetails(@Param("id") Long id);


    @Query(value="select  " +
        "          si.id, " +
        "          si.sequence, " +
        "          si.quantity, " +
        "          si.description, " +
        "          si.shipment_id as shipmentId, " +
        "          si.product_id as productId, " +
        "          p.image as image " +
        "from shipment_item si  " +
        "left join shop.product p on p.ref = si.product_id  " +
        "left join packaging_content pc on pc.shipment_item_id = si.id  " +
        "where pc.pkg_id = :id", nativeQuery = true)
    List<ShipmentItemDetails> findItemsInPkgForShipmentDetails(@Param("id") Long id);

    
    @Query (value="select oi.id, oi.product_name as description, oi.image, o.reference,  pui.purchase_id as po, o.created_date as orderDate, o.invoice_date as invoiceDate, p.created_date as purchaseDate,  " +
            "pr.url, pr.sku, m2.name as merchant, p.merchant_id as merchantId, " +
            "(select group_concat(concat(s2.id,':',s2.tracking_num,':',s2.shipment_method,':',s2.shipment_status,':',ifnull(m.name,''))) from purchase_shipment ps left join shipment_item si ON si.id = ps.shipment_item_id left JOIN shipment s2 on si.shipment_id  = s2.id and s2.shipment_type = 'TRANSIT' left join shop.merchant m on CONCAT('m', m.id) = s2.`_to`   where ps.purchase_item_id = pui.id group by pui.id ) as transitShipments,  " +
            "(select group_concat(concat(s2.id,':',s2.tracking_num,':',s2.shipment_method,':',s2.shipment_status,':',ifnull(m.name,'Badals'))) from purchase_shipment ps left join shipment_item si ON si.id = ps.shipment_item_id left JOIN shipment s2 on si.shipment_id  = s2.id and s2.shipment_type = 'PURCHASE'  left join shop.merchant m on CONCAT('m', m.id) = s2.`_to`   where ps.purchase_item_id = pui.id group by pui.id ) as purchaseShipments,  " +
            "(select group_concat(concat(ifnull(s3.id,'0'),':',s3.tracking_num,':',ifnull(s3.shipment_method,'BADALS'),':',s3.shipment_status)) from order_shipment os left join shipment_item si1 on si1.id = os.shipment_item_id left join shipment s3 on si1.shipment_id  = s3.id and s3.shipment_type = 'CUSTOMER' where os.order_item_id = oi.id group by oi.id) as customerShipments, " +
            "ifnull((select ifnull(sum(si1.quantity),0) from order_shipment os left join shipment_item si1 on si1.id = os.shipment_item_id left join shipment s3 on si1.shipment_id  = s3.id and s3.shipment_type = 'CUSTOMER' where os.order_item_id = oi.id group by oi.id),0) as delivered, " +
            "oi.quantity " +
            "from shop.order_item oi left JOIN shop.jhi_order o ON oi.order_id = o.id    " +
            "left JOIN shop.purchase_item_order_item pioi ON pioi.order_item_id = oi.id   " +
            "left join shop.purchase_item pui on pui.id = pioi.purchase_item_id   " +
            "left join shop.purchase p on p.id = pui.purchase_id " +
            "left join shop.merchant m2 on m2.id = p.merchant_id " +
            "left join shop.product pr on pr.`ref` = oi.product_id " +
            "where " +
            "(:noPo = 0 || pui.purchase_id is null) " +
            "and (:ref is null or o.reference = :ref) " +
            "and oi.quantity > 0 " +
            "and (o.reference = :ref or (o.state in ('PAYMENT_ACCEPTED', 'DELIVERED') and o.created_date > '2020-11-01' and o.created_date < DATE_SUB(NOW(), INTERVAL 4 DAY) )) " +
            "group by oi.id having " +
            "(:showall = 1 or delivered < quantity) " +
            "and (:poNoTransit = 0 or (po > 0 and transitShipments is null and purchaseShipments is null)) " +
            "and (:longTransit = 0 or (po > 0 and transitShipments is not null and purchaseShipments is null and orderDate >  DATE_SUB(NOW(), INTERVAL 7 DAY) )) " +
            "and (:lost = 0 or (po > 0 and transitShipments is not null and purchaseShipments is not null and (instr(purchaseShipments,'ACCEPTED') or  instr(purchaseShipments,'CLOSED')) and customerShipments is null )) \n" +
            "limit 500 ", nativeQuery = true)
    List<ItemTracking> trackByItem(@Param("ref") String ref, @Param("showall") int showall, @Param("noPo") int noPo,  @Param("poNoTransit") int poNoTransit, @Param("longTransit") int longTransit, @Param("lost") int lost);
}
