package com.badals.admin.service;

import com.badals.admin.domain.OrderShipment;
import com.badals.admin.repository.OrderShipmentRepository;
import com.badals.admin.service.dto.OrderShipmentDTO;
import com.badals.admin.service.mapper.OrderShipmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link OrderShipment}.
 */
@Service
@Transactional
public class OrderShipmentService {

    private final Logger log = LoggerFactory.getLogger(OrderShipmentService.class);

    private final OrderShipmentRepository orderShipmentRepository;

    private final OrderShipmentMapper orderShipmentMapper;

    public OrderShipmentService(OrderShipmentRepository orderShipmentRepository, OrderShipmentMapper orderShipmentMapper) {
        this.orderShipmentRepository = orderShipmentRepository;
        this.orderShipmentMapper = orderShipmentMapper;
    }

    /**
     * Save a orderShipment.
     *
     * @param orderShipmentDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderShipmentDTO save(OrderShipmentDTO orderShipmentDTO) {
        log.debug("Request to save OrderShipment : {}", orderShipmentDTO);
        OrderShipment orderShipment = orderShipmentMapper.toEntity(orderShipmentDTO);
        orderShipment = orderShipmentRepository.save(orderShipment);
        return orderShipmentMapper.toDto(orderShipment);
    }

    /**
     * Get all the orderShipments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderShipmentDTO> findAll() {
        log.debug("Request to get all OrderShipments");
        return orderShipmentRepository.findAll().stream()
            .map(orderShipmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one orderShipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderShipmentDTO> findOne(Long id) {
        log.debug("Request to get OrderShipment : {}", id);
        return orderShipmentRepository.findById(id)
            .map(orderShipmentMapper::toDto);
    }

    /**
     * Delete the orderShipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OrderShipment : {}", id);
        orderShipmentRepository.deleteById(id);
    }
}
