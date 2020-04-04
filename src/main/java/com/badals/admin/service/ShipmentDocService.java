package com.badals.admin.service;

import com.badals.admin.domain.ShipmentDoc;
import com.badals.admin.repository.ShipmentDocRepository;
import com.badals.admin.service.dto.ShipmentDocDTO;
import com.badals.admin.service.mapper.ShipmentDocMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ShipmentDoc}.
 */
@Service
@Transactional
public class ShipmentDocService {

    private final Logger log = LoggerFactory.getLogger(ShipmentDocService.class);

    private final ShipmentDocRepository shipmentDocRepository;

    private final ShipmentDocMapper shipmentDocMapper;

    public ShipmentDocService(ShipmentDocRepository shipmentDocRepository, ShipmentDocMapper shipmentDocMapper) {
        this.shipmentDocRepository = shipmentDocRepository;
        this.shipmentDocMapper = shipmentDocMapper;
    }

    /**
     * Save a shipmentDoc.
     *
     * @param shipmentDocDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentDocDTO save(ShipmentDocDTO shipmentDocDTO) {
        log.debug("Request to save ShipmentDoc : {}", shipmentDocDTO);
        ShipmentDoc shipmentDoc = shipmentDocMapper.toEntity(shipmentDocDTO);
        shipmentDoc = shipmentDocRepository.save(shipmentDoc);
        return shipmentDocMapper.toDto(shipmentDoc);
    }

    /**
     * Get all the shipmentDocs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShipmentDocDTO> findAll() {
        log.debug("Request to get all ShipmentDocs");
        return shipmentDocRepository.findAll().stream()
            .map(shipmentDocMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one shipmentDoc by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentDocDTO> findOne(Long id) {
        log.debug("Request to get ShipmentDoc : {}", id);
        return shipmentDocRepository.findById(id)
            .map(shipmentDocMapper::toDto);
    }

    /**
     * Delete the shipmentDoc by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShipmentDoc : {}", id);
        shipmentDocRepository.deleteById(id);
    }
}
