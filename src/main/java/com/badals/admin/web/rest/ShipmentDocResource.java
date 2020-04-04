package com.badals.admin.web.rest;

import com.badals.admin.service.ShipmentDocService;
import com.badals.admin.web.rest.errors.BadRequestAlertException;
import com.badals.admin.service.dto.ShipmentDocDTO;

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
 * REST controller for managing {@link com.badals.admin.domain.ShipmentDoc}.
 */
@RestController
@RequestMapping("/api")
public class ShipmentDocResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentDocResource.class);

    private static final String ENTITY_NAME = "adminShipmentDoc";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentDocService shipmentDocService;

    public ShipmentDocResource(ShipmentDocService shipmentDocService) {
        this.shipmentDocService = shipmentDocService;
    }

    /**
     * {@code POST  /shipment-docs} : Create a new shipmentDoc.
     *
     * @param shipmentDocDTO the shipmentDocDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentDocDTO, or with status {@code 400 (Bad Request)} if the shipmentDoc has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shipment-docs")
    public ResponseEntity<ShipmentDocDTO> createShipmentDoc(@RequestBody ShipmentDocDTO shipmentDocDTO) throws URISyntaxException {
        log.debug("REST request to save ShipmentDoc : {}", shipmentDocDTO);
        if (shipmentDocDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentDoc cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShipmentDocDTO result = shipmentDocService.save(shipmentDocDTO);
        return ResponseEntity.created(new URI("/api/shipment-docs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shipment-docs} : Updates an existing shipmentDoc.
     *
     * @param shipmentDocDTO the shipmentDocDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentDocDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentDocDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentDocDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shipment-docs")
    public ResponseEntity<ShipmentDocDTO> updateShipmentDoc(@RequestBody ShipmentDocDTO shipmentDocDTO) throws URISyntaxException {
        log.debug("REST request to update ShipmentDoc : {}", shipmentDocDTO);
        if (shipmentDocDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShipmentDocDTO result = shipmentDocService.save(shipmentDocDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentDocDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shipment-docs} : get all the shipmentDocs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentDocs in body.
     */
    @GetMapping("/shipment-docs")
    public List<ShipmentDocDTO> getAllShipmentDocs() {
        log.debug("REST request to get all ShipmentDocs");
        return shipmentDocService.findAll();
    }

    /**
     * {@code GET  /shipment-docs/:id} : get the "id" shipmentDoc.
     *
     * @param id the id of the shipmentDocDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentDocDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shipment-docs/{id}")
    public ResponseEntity<ShipmentDocDTO> getShipmentDoc(@PathVariable Long id) {
        log.debug("REST request to get ShipmentDoc : {}", id);
        Optional<ShipmentDocDTO> shipmentDocDTO = shipmentDocService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentDocDTO);
    }

    /**
     * {@code DELETE  /shipment-docs/:id} : delete the "id" shipmentDoc.
     *
     * @param id the id of the shipmentDocDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shipment-docs/{id}")
    public ResponseEntity<Void> deleteShipmentDoc(@PathVariable Long id) {
        log.debug("REST request to delete ShipmentDoc : {}", id);
        shipmentDocService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
