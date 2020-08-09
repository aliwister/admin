package com.badals.admin.service;

import com.badals.admin.domain.Order;
import com.badals.admin.domain.enumeration.OrderState;
import com.badals.admin.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order setStatus(String id, OrderState state) {
        Order order = orderRepository.findByReference(id).get();
        order.setOrderState(state);
        return orderRepository.save(order);
    }
}
