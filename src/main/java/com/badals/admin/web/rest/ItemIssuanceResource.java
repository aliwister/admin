package com.badals.admin.web.rest;

import com.badals.admin.service.ItemIssuanceService;
import com.badals.admin.web.rest.errors.BadRequestAlertException;
import com.badals.admin.service.dto.ItemIssuanceDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.badals.admin.domain.ItemIssuance}.
 */
@RestController
@RequestMapping("/api")
public class ItemIssuanceResource {

    private final Logger log = LoggerFactory.getLogger(ItemIssuanceResource.class);

    private static final String ENTITY_NAME = "adminItemIssuance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemIssuanceService itemIssuanceService;

    public ItemIssuanceResource(ItemIssuanceService itemIssuanceService) {
        this.itemIssuanceService = itemIssuanceService;
    }

    /**
     * {@code POST  /item-issuances} : Create a new itemIssuance.
     *
     * @param itemIssuanceDTO the itemIssuanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemIssuanceDTO, or with status {@code 400 (Bad Request)} if the itemIssuance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-issuances")
    public ResponseEntity<ItemIssuanceDTO> createItemIssuance(@RequestBody ItemIssuanceDTO itemIssuanceDTO) throws URISyntaxException {
        log.debug("REST request to save ItemIssuance : {}", itemIssuanceDTO);
        if (itemIssuanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemIssuance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemIssuanceDTO result = itemIssuanceService.save(itemIssuanceDTO);
        return ResponseEntity.created(new URI("/api/item-issuances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-issuances} : Updates an existing itemIssuance.
     *
     * @param itemIssuanceDTO the itemIssuanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemIssuanceDTO,
     * or with status {@code 400 (Bad Request)} if the itemIssuanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemIssuanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-issuances")
    public ResponseEntity<ItemIssuanceDTO> updateItemIssuance(@RequestBody ItemIssuanceDTO itemIssuanceDTO) throws URISyntaxException {
        log.debug("REST request to update ItemIssuance : {}", itemIssuanceDTO);
        if (itemIssuanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ItemIssuanceDTO result = itemIssuanceService.save(itemIssuanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemIssuanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /item-issuances} : get all the itemIssuances.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemIssuances in body.
     */
    @GetMapping("/item-issuances")
    public List<ItemIssuanceDTO> getAllItemIssuances() {
        log.debug("REST request to get all ItemIssuances");
        return itemIssuanceService.findAll();
    }

    /**
     * {@code GET  /item-issuances/:id} : get the "id" itemIssuance.
     *
     * @param id the id of the itemIssuanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemIssuanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-issuances/{id}")
    public ResponseEntity<ItemIssuanceDTO> getItemIssuance(@PathVariable Long id) {
        log.debug("REST request to get ItemIssuance : {}", id);
        Optional<ItemIssuanceDTO> itemIssuanceDTO = itemIssuanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemIssuanceDTO);
    }

    /**
     * {@code DELETE  /item-issuances/:id} : delete the "id" itemIssuance.
     *
     * @param id the id of the itemIssuanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-issuances/{id}")
    public ResponseEntity<Void> deleteItemIssuance(@PathVariable Long id) {
        log.debug("REST request to delete ItemIssuance : {}", id);
        itemIssuanceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
