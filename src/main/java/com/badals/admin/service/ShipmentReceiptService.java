package com.badals.admin.service;

import com.badals.admin.domain.ShipmentReceipt;
import com.badals.admin.repository.ShipmentReceiptRepository;
import com.badals.admin.service.dto.ShipmentReceiptDTO;
import com.badals.admin.service.mapper.ShipmentReceiptMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ShipmentReceipt}.
 */
@Service
@Transactional
public class ShipmentReceiptService {

    private final Logger log = LoggerFactory.getLogger(ShipmentReceiptService.class);

    private final ShipmentReceiptRepository shipmentReceiptRepository;

    private final ShipmentReceiptMapper shipmentReceiptMapper;

    public ShipmentReceiptService(ShipmentReceiptRepository shipmentReceiptRepository, ShipmentReceiptMapper shipmentReceiptMapper) {
        this.shipmentReceiptRepository = shipmentReceiptRepository;
        this.shipmentReceiptMapper = shipmentReceiptMapper;
    }

    /**
     * Save a shipmentReceipt.
     *
     * @param shipmentReceiptDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentReceiptDTO save(ShipmentReceiptDTO shipmentReceiptDTO) {
        log.debug("Request to save ShipmentReceipt : {}", shipmentReceiptDTO);
        ShipmentReceipt shipmentReceipt = shipmentReceiptMapper.toEntity(shipmentReceiptDTO);
        shipmentReceipt = shipmentReceiptRepository.save(shipmentReceipt);
        return shipmentReceiptMapper.toDto(shipmentReceipt);
    }

    /**
     * Get all the shipmentReceipts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShipmentReceiptDTO> findAll() {
        log.debug("Request to get all ShipmentReceipts");
        return shipmentReceiptRepository.findAll().stream()
            .map(shipmentReceiptMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one shipmentReceipt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentReceiptDTO> findOne(Long id) {
        log.debug("Request to get ShipmentReceipt : {}", id);
        return shipmentReceiptRepository.findById(id)
            .map(shipmentReceiptMapper::toDto);
    }

    /**
     * Delete the shipmentReceipt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShipmentReceipt : {}", id);
        shipmentReceiptRepository.deleteById(id);
    }
}
