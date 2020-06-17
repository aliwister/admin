package com.badals.admin.service;

import com.badals.admin.domain.ShipmentEvent;
import com.badals.admin.repository.ShipmentEventRepository;
import com.badals.admin.service.dto.ShipmentEventDTO;
import com.badals.admin.service.mapper.ShipmentEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ShipmentEvent}.
 */
@Service
@Transactional
public class ShipmentEventService {

    private final Logger log = LoggerFactory.getLogger(ShipmentEventService.class);

    private final ShipmentEventRepository shipmentEventRepository;

    private final ShipmentEventMapper shipmentEventMapper;

    public ShipmentEventService(ShipmentEventRepository shipmentEventRepository, ShipmentEventMapper shipmentEventMapper) {
        this.shipmentEventRepository = shipmentEventRepository;
        this.shipmentEventMapper = shipmentEventMapper;
    }

    /**
     * Save a shipmentEvent.
     *
     * @param shipmentEventDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentEventDTO save(ShipmentEventDTO shipmentEventDTO) {
        log.debug("Request to save ShipmentEvent : {}", shipmentEventDTO);
        ShipmentEvent shipmentEvent = shipmentEventMapper.toEntity(shipmentEventDTO);
        shipmentEvent = shipmentEventRepository.save(shipmentEvent);
        return shipmentEventMapper.toDto(shipmentEvent);
    }

    /**
     * Get all the shipmentEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShipmentEventDTO> findAll() {
        log.debug("Request to get all ShipmentEvents");
        return shipmentEventRepository.findAll().stream()
            .map(shipmentEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one shipmentEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentEventDTO> findOne(Long id) {
        log.debug("Request to get ShipmentEvent : {}", id);
        return shipmentEventRepository.findById(id)
            .map(shipmentEventMapper::toDto);
    }

    /**
     * Delete the shipmentEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShipmentEvent : {}", id);
        shipmentEventRepository.deleteById(id);
    }
}
