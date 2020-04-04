package com.badals.admin.web.rest;

import com.badals.admin.service.ShipmentItemService;
import com.badals.admin.web.rest.errors.BadRequestAlertException;
import com.badals.admin.service.dto.ShipmentItemDTO;

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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.badals.admin.domain.ShipmentItem}.
 */
@RestController
@RequestMapping("/api")
public class ShipmentItemResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentItemResource.class);

    private static final String ENTITY_NAME = "adminShipmentItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentItemService shipmentItemService;

    public ShipmentItemResource(ShipmentItemService shipmentItemService) {
        this.shipmentItemService = shipmentItemService;
    }

    /**
     * {@code POST  /shipment-items} : Create a new shipmentItem.
     *
     * @param shipmentItemDTO the shipmentItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentItemDTO, or with status {@code 400 (Bad Request)} if the shipmentItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shipment-items")
    public ResponseEntity<ShipmentItemDTO> createShipmentItem(@RequestBody ShipmentItemDTO shipmentItemDTO) throws URISyntaxException {
        log.debug("REST request to save ShipmentItem : {}", shipmentItemDTO);
        if (shipmentItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShipmentItemDTO result = shipmentItemService.save(shipmentItemDTO);
        return ResponseEntity.created(new URI("/api/shipment-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shipment-items} : Updates an existing shipmentItem.
     *
     * @param shipmentItemDTO the shipmentItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentItemDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shipment-items")
    public ResponseEntity<ShipmentItemDTO> updateShipmentItem(@RequestBody ShipmentItemDTO shipmentItemDTO) throws URISyntaxException {
        log.debug("REST request to update ShipmentItem : {}", shipmentItemDTO);
        if (shipmentItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShipmentItemDTO result = shipmentItemService.save(shipmentItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shipment-items} : get all the shipmentItems.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentItems in body.
     */
    @GetMapping("/shipment-items")
    public List<ShipmentItemDTO> getAllShipmentItems() {
        log.debug("REST request to get all ShipmentItems");
        return shipmentItemService.findAll();
    }

    /**
     * {@code GET  /shipment-items/:id} : get the "id" shipmentItem.
     *
     * @param id the id of the shipmentItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shipment-items/{id}")
    public ResponseEntity<ShipmentItemDTO> getShipmentItem(@PathVariable Long id) {
        log.debug("REST request to get ShipmentItem : {}", id);
        Optional<ShipmentItemDTO> shipmentItemDTO = shipmentItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentItemDTO);
    }

    /**
     * {@code DELETE  /shipment-items/:id} : delete the "id" shipmentItem.
     *
     * @param id the id of the shipmentItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shipment-items/{id}")
    public ResponseEntity<Void> deleteShipmentItem(@PathVariable Long id) {
        log.debug("REST request to delete ShipmentItem : {}", id);
        shipmentItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/shipment-items?query=:query} : search for the shipmentItem corresponding
     * to the query.
     *
     * @param query the query of the shipmentItem search.
     * @return the result of the search.
     */
/*    @GetMapping("/_search/shipment-items")
    public List<ShipmentItemDTO> searchShipmentItems(@RequestParam String query) {
        log.debug("REST request to search ShipmentItems for query {}", query);
        return shipmentItemService.search(query);
    }*/
}
