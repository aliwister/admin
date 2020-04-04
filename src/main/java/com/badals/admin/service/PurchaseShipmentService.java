package com.badals.admin.service;

import com.badals.admin.domain.PurchaseShipment;
import com.badals.admin.repository.PurchaseShipmentRepository;
import com.badals.admin.service.dto.PurchaseShipmentDTO;
import com.badals.admin.service.mapper.PurchaseShipmentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PurchaseShipment}.
 */
@Service
@Transactional
public class PurchaseShipmentService {

    private final Logger log = LoggerFactory.getLogger(PurchaseShipmentService.class);

    private final PurchaseShipmentRepository purchaseShipmentRepository;

    private final PurchaseShipmentMapper purchaseShipmentMapper;

    public PurchaseShipmentService(PurchaseShipmentRepository purchaseShipmentRepository, PurchaseShipmentMapper purchaseShipmentMapper) {
        this.purchaseShipmentRepository = purchaseShipmentRepository;
        this.purchaseShipmentMapper = purchaseShipmentMapper;
    }

    /**
     * Save a purchaseShipment.
     *
     * @param purchaseShipmentDTO the entity to save.
     * @return the persisted entity.
     */
    public PurchaseShipmentDTO save(PurchaseShipmentDTO purchaseShipmentDTO) {
        log.debug("Request to save PurchaseShipment : {}", purchaseShipmentDTO);
        PurchaseShipment purchaseShipment = purchaseShipmentMapper.toEntity(purchaseShipmentDTO);
        purchaseShipment = purchaseShipmentRepository.save(purchaseShipment);
        return purchaseShipmentMapper.toDto(purchaseShipment);
    }

    /**
     * Get all the purchaseShipments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PurchaseShipmentDTO> findAll() {
        log.debug("Request to get all PurchaseShipments");
        return purchaseShipmentRepository.findAll().stream()
            .map(purchaseShipmentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one purchaseShipment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PurchaseShipmentDTO> findOne(Long id) {
        log.debug("Request to get PurchaseShipment : {}", id);
        return purchaseShipmentRepository.findById(id)
            .map(purchaseShipmentMapper::toDto);
    }

    /**
     * Delete the purchaseShipment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PurchaseShipment : {}", id);
        purchaseShipmentRepository.deleteById(id);
    }
}
