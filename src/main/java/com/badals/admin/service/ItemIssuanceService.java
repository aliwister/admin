package com.badals.admin.service;

import com.badals.admin.domain.ItemIssuance;
import com.badals.admin.repository.ItemIssuanceRepository;
import com.badals.admin.service.dto.ItemIssuanceDTO;
import com.badals.admin.service.mapper.ItemIssuanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ItemIssuance}.
 */
@Service
@Transactional
public class ItemIssuanceService {

    private final Logger log = LoggerFactory.getLogger(ItemIssuanceService.class);

    private final ItemIssuanceRepository itemIssuanceRepository;

    private final ItemIssuanceMapper itemIssuanceMapper;

    public ItemIssuanceService(ItemIssuanceRepository itemIssuanceRepository, ItemIssuanceMapper itemIssuanceMapper) {
        this.itemIssuanceRepository = itemIssuanceRepository;
        this.itemIssuanceMapper = itemIssuanceMapper;
    }

    /**
     * Save a itemIssuance.
     *
     * @param itemIssuanceDTO the entity to save.
     * @return the persisted entity.
     */
    public ItemIssuanceDTO save(ItemIssuanceDTO itemIssuanceDTO) {
        log.debug("Request to save ItemIssuance : {}", itemIssuanceDTO);
        ItemIssuance itemIssuance = itemIssuanceMapper.toEntity(itemIssuanceDTO);
        itemIssuance = itemIssuanceRepository.save(itemIssuance);
        return itemIssuanceMapper.toDto(itemIssuance);
    }

    /**
     * Get all the itemIssuances.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ItemIssuanceDTO> findAll() {
        log.debug("Request to get all ItemIssuances");
        return itemIssuanceRepository.findAll().stream()
            .map(itemIssuanceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one itemIssuance by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemIssuanceDTO> findOne(Long id) {
        log.debug("Request to get ItemIssuance : {}", id);
        return itemIssuanceRepository.findById(id)
            .map(itemIssuanceMapper::toDto);
    }

    /**
     * Delete the itemIssuance by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ItemIssuance : {}", id);
        itemIssuanceRepository.deleteById(id);
    }
}
