package com.badals.admin.service;

import com.badals.admin.domain.ShipmentItem;
import com.badals.admin.repository.ShipmentItemRepository;
//import com.badals.admin.repository.search.ShipmentItemSearchRepository;
import com.badals.admin.service.dto.ShipmentItemDTO;
import com.badals.admin.service.mapper.ShipmentItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link ShipmentItem}.
 */
@Service
@Transactional
public class ShipmentItemService {

    private final Logger log = LoggerFactory.getLogger(ShipmentItemService.class);

    private final ShipmentItemRepository shipmentItemRepository;

    private final ShipmentItemMapper shipmentItemMapper;

    //private final ShipmentItemSearchRepository shipmentItemSearchRepository;

    public ShipmentItemService(ShipmentItemRepository shipmentItemRepository, ShipmentItemMapper shipmentItemMapper/*, ShipmentItemSearchRepository shipmentItemSearchRepository*/) {
        this.shipmentItemRepository = shipmentItemRepository;
        this.shipmentItemMapper = shipmentItemMapper;
        //this.shipmentItemSearchRepository = shipmentItemSearchRepository;
    }

    /**
     * Save a shipmentItem.
     *
     * @param shipmentItemDTO the entity to save.
     * @return the persisted entity.
     */
    public ShipmentItemDTO save(ShipmentItemDTO shipmentItemDTO) {
        log.debug("Request to save ShipmentItem : {}", shipmentItemDTO);
        ShipmentItem shipmentItem = shipmentItemMapper.toEntity(shipmentItemDTO);
        shipmentItem = shipmentItemRepository.save(shipmentItem);
        ShipmentItemDTO result = shipmentItemMapper.toDto(shipmentItem);
        //shipmentItemSearchRepository.save(shipmentItem);
        return result;
    }

    /**
     * Get all the shipmentItems.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShipmentItemDTO> findAll() {
        log.debug("Request to get all ShipmentItems");
        return shipmentItemRepository.findAll().stream()
            .map(shipmentItemMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one shipmentItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShipmentItemDTO> findOne(Long id) {
        log.debug("Request to get ShipmentItem : {}", id);
        return shipmentItemRepository.findById(id)
            .map(shipmentItemMapper::toDto);
    }

    /**
     * Delete the shipmentItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShipmentItem : {}", id);
        shipmentItemRepository.deleteById(id);
        //shipmentItemSearchRepository.deleteById(id);
    }

    /**
     * Search for the shipmentItem corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
/*    @Transactional(readOnly = true)
    public List<ShipmentItemDTO> search(String query) {
        log.debug("Request to search ShipmentItems for query {}", query);
        return StreamSupport
            .stream(shipmentItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(shipmentItemMapper::toDto)
            .collect(Collectors.toList());
    }*/
}
