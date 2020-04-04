package com.badals.admin.web.rest;

import com.badals.admin.service.PurchaseShipmentService;
import com.badals.admin.web.rest.errors.BadRequestAlertException;
import com.badals.admin.service.dto.PurchaseShipmentDTO;

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
 * REST controller for managing {@link com.badals.admin.domain.PurchaseShipment}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseShipmentResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseShipmentResource.class);

    private static final String ENTITY_NAME = "adminPurchaseShipment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseShipmentService purchaseShipmentService;

    public PurchaseShipmentResource(PurchaseShipmentService purchaseShipmentService) {
        this.purchaseShipmentService = purchaseShipmentService;
    }

    /**
     * {@code POST  /purchase-shipments} : Create a new purchaseShipment.
     *
     * @param purchaseShipmentDTO the purchaseShipmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseShipmentDTO, or with status {@code 400 (Bad Request)} if the purchaseShipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-shipments")
    public ResponseEntity<PurchaseShipmentDTO> createPurchaseShipment(@RequestBody PurchaseShipmentDTO purchaseShipmentDTO) throws URISyntaxException {
        log.debug("REST request to save PurchaseShipment : {}", purchaseShipmentDTO);
        if (purchaseShipmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchaseShipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseShipmentDTO result = purchaseShipmentService.save(purchaseShipmentDTO);
        return ResponseEntity.created(new URI("/api/purchase-shipments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-shipments} : Updates an existing purchaseShipment.
     *
     * @param purchaseShipmentDTO the purchaseShipmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseShipmentDTO,
     * or with status {@code 400 (Bad Request)} if the purchaseShipmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseShipmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-shipments")
    public ResponseEntity<PurchaseShipmentDTO> updatePurchaseShipment(@RequestBody PurchaseShipmentDTO purchaseShipmentDTO) throws URISyntaxException {
        log.debug("REST request to update PurchaseShipment : {}", purchaseShipmentDTO);
        if (purchaseShipmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchaseShipmentDTO result = purchaseShipmentService.save(purchaseShipmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseShipmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /purchase-shipments} : get all the purchaseShipments.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseShipments in body.
     */
    @GetMapping("/purchase-shipments")
    public List<PurchaseShipmentDTO> getAllPurchaseShipments() {
        log.debug("REST request to get all PurchaseShipments");
        return purchaseShipmentService.findAll();
    }

    /**
     * {@code GET  /purchase-shipments/:id} : get the "id" purchaseShipment.
     *
     * @param id the id of the purchaseShipmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseShipmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-shipments/{id}")
    public ResponseEntity<PurchaseShipmentDTO> getPurchaseShipment(@PathVariable Long id) {
        log.debug("REST request to get PurchaseShipment : {}", id);
        Optional<PurchaseShipmentDTO> purchaseShipmentDTO = purchaseShipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseShipmentDTO);
    }

    /**
     * {@code DELETE  /purchase-shipments/:id} : delete the "id" purchaseShipment.
     *
     * @param id the id of the purchaseShipmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-shipments/{id}")
    public ResponseEntity<Void> deletePurchaseShipment(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseShipment : {}", id);
        purchaseShipmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
