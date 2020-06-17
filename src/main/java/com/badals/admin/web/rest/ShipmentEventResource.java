package com.badals.admin.web.rest;

import com.badals.admin.service.ShipmentEventService;
import com.badals.admin.web.rest.errors.BadRequestAlertException;
import com.badals.admin.service.dto.ShipmentEventDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.badals.admin.domain.ShipmentEvent}.
 */
@RestController
@RequestMapping("/api")
public class ShipmentEventResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentEventResource.class);

    private static final String ENTITY_NAME = "adminShipmentEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentEventService shipmentEventService;

    public ShipmentEventResource(ShipmentEventService shipmentEventService) {
        this.shipmentEventService = shipmentEventService;
    }

    /**
     * {@code POST  /shipment-events} : Create a new shipmentEvent.
     *
     * @param shipmentEventDTO the shipmentEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentEventDTO, or with status {@code 400 (Bad Request)} if the shipmentEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shipment-events")
    public ResponseEntity<ShipmentEventDTO> createShipmentEvent(@Valid @RequestBody ShipmentEventDTO shipmentEventDTO) throws URISyntaxException {
        log.debug("REST request to save ShipmentEvent : {}", shipmentEventDTO);
        if (shipmentEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShipmentEventDTO result = shipmentEventService.save(shipmentEventDTO);
        return ResponseEntity.created(new URI("/api/shipment-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shipment-events} : Updates an existing shipmentEvent.
     *
     * @param shipmentEventDTO the shipmentEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentEventDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shipment-events")
    public ResponseEntity<ShipmentEventDTO> updateShipmentEvent(@Valid @RequestBody ShipmentEventDTO shipmentEventDTO) throws URISyntaxException {
        log.debug("REST request to update ShipmentEvent : {}", shipmentEventDTO);
        if (shipmentEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShipmentEventDTO result = shipmentEventService.save(shipmentEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentEventDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shipment-events} : get all the shipmentEvents.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentEvents in body.
     */
    @GetMapping("/shipment-events")
    public List<ShipmentEventDTO> getAllShipmentEvents() {
        log.debug("REST request to get all ShipmentEvents");
        return shipmentEventService.findAll();
    }

    /**
     * {@code GET  /shipment-events/:id} : get the "id" shipmentEvent.
     *
     * @param id the id of the shipmentEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shipment-events/{id}")
    public ResponseEntity<ShipmentEventDTO> getShipmentEvent(@PathVariable Long id) {
        log.debug("REST request to get ShipmentEvent : {}", id);
        Optional<ShipmentEventDTO> shipmentEventDTO = shipmentEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentEventDTO);
    }

    /**
     * {@code DELETE  /shipment-events/:id} : delete the "id" shipmentEvent.
     *
     * @param id the id of the shipmentEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shipment-events/{id}")
    public ResponseEntity<Void> deleteShipmentEvent(@PathVariable Long id) {
        log.debug("REST request to delete ShipmentEvent : {}", id);
        shipmentEventService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
