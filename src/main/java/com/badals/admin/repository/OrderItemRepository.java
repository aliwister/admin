package com.badals.admin.repository;


import com.badals.admin.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrderItem entity.
 */

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("from OrderItem oi join fetch oi.order o join fetch o.customer where oi.id = ?1")
    public OrderItem findForSorting(Long id);
}
