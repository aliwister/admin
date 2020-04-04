package com.badals.admin.web.rest;

import com.badals.admin.service.ShipmentReceiptService;
import com.badals.admin.web.rest.errors.BadRequestAlertException;
import com.badals.admin.service.dto.ShipmentReceiptDTO;

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
 * REST controller for managing {@link com.badals.admin.domain.ShipmentReceipt}.
 */
@RestController
@RequestMapping("/api")
public class ShipmentReceiptResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentReceiptResource.class);

    private static final String ENTITY_NAME = "adminShipmentReceipt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentReceiptService shipmentReceiptService;

    public ShipmentReceiptResource(ShipmentReceiptService shipmentReceiptService) {
        this.shipmentReceiptService = shipmentReceiptService;
    }

    /**
     * {@code POST  /shipment-receipts} : Create a new shipmentReceipt.
     *
     * @param shipmentReceiptDTO the shipmentReceiptDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentReceiptDTO, or with status {@code 400 (Bad Request)} if the shipmentReceipt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shipment-receipts")
    public ResponseEntity<ShipmentReceiptDTO> createShipmentReceipt(@RequestBody ShipmentReceiptDTO shipmentReceiptDTO) throws URISyntaxException {
        log.debug("REST request to save ShipmentReceipt : {}", shipmentReceiptDTO);
        if (shipmentReceiptDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentReceipt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShipmentReceiptDTO result = shipmentReceiptService.save(shipmentReceiptDTO);
        return ResponseEntity.created(new URI("/api/shipment-receipts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shipment-receipts} : Updates an existing shipmentReceipt.
     *
     * @param shipmentReceiptDTO the shipmentReceiptDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentReceiptDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentReceiptDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentReceiptDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shipment-receipts")
    public ResponseEntity<ShipmentReceiptDTO> updateShipmentReceipt(@RequestBody ShipmentReceiptDTO shipmentReceiptDTO) throws URISyntaxException {
        log.debug("REST request to update ShipmentReceipt : {}", shipmentReceiptDTO);
        if (shipmentReceiptDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShipmentReceiptDTO result = shipmentReceiptService.save(shipmentReceiptDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentReceiptDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shipment-receipts} : get all the shipmentReceipts.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentReceipts in body.
     */
    @GetMapping("/shipment-receipts")
    public List<ShipmentReceiptDTO> getAllShipmentReceipts() {
        log.debug("REST request to get all ShipmentReceipts");
        return shipmentReceiptService.findAll();
    }

    /**
     * {@code GET  /shipment-receipts/:id} : get the "id" shipmentReceipt.
     *
     * @param id the id of the shipmentReceiptDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentReceiptDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shipment-receipts/{id}")
    public ResponseEntity<ShipmentReceiptDTO> getShipmentReceipt(@PathVariable Long id) {
        log.debug("REST request to get ShipmentReceipt : {}", id);
        Optional<ShipmentReceiptDTO> shipmentReceiptDTO = shipmentReceiptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentReceiptDTO);
    }

    /**
     * {@code DELETE  /shipment-receipts/:id} : delete the "id" shipmentReceipt.
     *
     * @param id the id of the shipmentReceiptDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shipment-receipts/{id}")
    public ResponseEntity<Void> deleteShipmentReceipt(@PathVariable Long id) {
        log.debug("REST request to delete ShipmentReceipt : {}", id);
        shipmentReceiptService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
