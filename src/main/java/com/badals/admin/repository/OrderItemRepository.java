package com.badals.admin.repository;


import com.badals.admin.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


/**
 * Spring Data  repository for the OrderItem entity.
 */

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("from OrderItem oi join fetch oi.order o join fetch o.customer where oi.id = ?1")
    public OrderItem findForSorting(Long id);

    @Query(value="SELECT (ifnull(SUM(os.quantity),0) + :quantity) > ifnull(oi.quantity,0) " +
        "FROM profileshop.order_item oi  " +
        "LEFT JOIN order_shipment os ON oi.id = os.order_item_id " +
        "JOIN profileshop.jhi_order o ON o.id = oi.order_id AND oi.id = :orderItemId " +
        "WHERE o.state IN ('DELIVERED','PAYMENT_ACCEPTED') " +
        "GROUP BY oi.id", nativeQuery = true)
    Integer checkFilled(@Param("orderItemId") Long orderItemId, @Param("quantity") BigDecimal quantity);
}
