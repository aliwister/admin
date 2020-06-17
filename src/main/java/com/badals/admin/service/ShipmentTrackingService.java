package com.badals.admin.service;

import com.badals.admin.domain. ShipmentTracking;
import com.badals.admin.repository. ShipmentTrackingRepository;
import com.badals.admin.service.dto. ShipmentTrackingDTO;
import com.badals.admin.service.mapper. ShipmentTrackingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link  ShipmentTracking}.
 */
@Service
@Transactional
public class  ShipmentTrackingService {

    private final Logger log = LoggerFactory.getLogger( ShipmentTrackingService.class);

    private final  ShipmentTrackingRepository shipmentStatusRepository;

    private final  ShipmentTrackingMapper shipmentTrackingMapper;

    public  ShipmentTrackingService( ShipmentTrackingRepository shipmentStatusRepository,  ShipmentTrackingMapper shipmentTrackingMapper) {
        this.shipmentStatusRepository = shipmentStatusRepository;
        this.shipmentTrackingMapper = shipmentTrackingMapper;
    }

    /**
     * Save a shipmentStatus.
     *
     * @param shipmentStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public  ShipmentTrackingDTO save( ShipmentTrackingDTO shipmentStatusDTO) {
        log.debug("Request to save  ShipmentTracking : {}", shipmentStatusDTO);
         ShipmentTracking shipmentStatus = shipmentTrackingMapper.toEntity(shipmentStatusDTO);
        shipmentStatus = shipmentStatusRepository.save(shipmentStatus);
        return shipmentTrackingMapper.toDto(shipmentStatus);
    }

    /**
     * Get all the shipmentStatuses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List< ShipmentTrackingDTO> findAll() {
        log.debug("Request to get all  ShipmentTrackinges");
        return shipmentStatusRepository.findAll().stream()
            .map(shipmentTrackingMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one shipmentStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional< ShipmentTrackingDTO> findOne(Long id) {
        log.debug("Request to get  ShipmentTracking : {}", id);
        return shipmentStatusRepository.findById(id)
            .map(shipmentTrackingMapper::toDto);
    }

    /**
     * Delete the shipmentStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete  ShipmentTracking : {}", id);
        shipmentStatusRepository.deleteById(id);
    }
}
