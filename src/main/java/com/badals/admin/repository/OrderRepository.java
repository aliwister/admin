package com.badals.admin.repository;


import com.badals.admin.domain.Order;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByReference(String reference);

    @Query(value = "SELECT ((o.total - SUM(p.amount))) AS balance " +
        "FROM profileshop.jhi_order o LEFT JOIN profileshop.payment p ON o.id = p.order_id " +
        "WHERE o.reference = :ref " +
        "GROUP BY o.reference", nativeQuery = true)
    BigDecimal getBalance(@Param("ref") String orderRef);
}
