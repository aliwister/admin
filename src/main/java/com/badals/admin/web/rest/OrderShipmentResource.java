package com.badals.admin.web.rest;

import com.badals.admin.service.OrderShipmentService;
import com.badals.admin.web.rest.errors.BadRequestAlertException;
import com.badals.admin.service.dto.OrderShipmentDTO;

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
 * REST controller for managing {@link com.badals.admin.domain.OrderShipment}.
 */
@RestController
@RequestMapping("/api")
public class OrderShipmentResource {

    private final Logger log = LoggerFactory.getLogger(OrderShipmentResource.class);

    private static final String ENTITY_NAME = "adminOrderShipment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderShipmentService orderShipmentService;

    public OrderShipmentResource(OrderShipmentService orderShipmentService) {
        this.orderShipmentService = orderShipmentService;
    }

    /**
     * {@code POST  /order-shipments} : Create a new orderShipment.
     *
     * @param orderShipmentDTO the orderShipmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderShipmentDTO, or with status {@code 400 (Bad Request)} if the orderShipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-shipments")
    public ResponseEntity<OrderShipmentDTO> createOrderShipment(@RequestBody OrderShipmentDTO orderShipmentDTO) throws URISyntaxException {
        log.debug("REST request to save OrderShipment : {}", orderShipmentDTO);
        if (orderShipmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderShipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderShipmentDTO result = orderShipmentService.save(orderShipmentDTO);
        return ResponseEntity.created(new URI("/api/order-shipments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-shipments} : Updates an existing orderShipment.
     *
     * @param orderShipmentDTO the orderShipmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderShipmentDTO,
     * or with status {@code 400 (Bad Request)} if the orderShipmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderShipmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-shipments")
    public ResponseEntity<OrderShipmentDTO> updateOrderShipment(@RequestBody OrderShipmentDTO orderShipmentDTO) throws URISyntaxException {
        log.debug("REST request to update OrderShipment : {}", orderShipmentDTO);
        if (orderShipmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrderShipmentDTO result = orderShipmentService.save(orderShipmentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderShipmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /order-shipments} : get all the orderShipments.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderShipments in body.
     */
    @GetMapping("/order-shipments")
    public List<OrderShipmentDTO> getAllOrderShipments() {
        log.debug("REST request to get all OrderShipments");
        return orderShipmentService.findAll();
    }

    /**
     * {@code GET  /order-shipments/:id} : get the "id" orderShipment.
     *
     * @param id the id of the orderShipmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderShipmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-shipments/{id}")
    public ResponseEntity<OrderShipmentDTO> getOrderShipment(@PathVariable Long id) {
        log.debug("REST request to get OrderShipment : {}", id);
        Optional<OrderShipmentDTO> orderShipmentDTO = orderShipmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderShipmentDTO);
    }

    /**
     * {@code DELETE  /order-shipments/:id} : delete the "id" orderShipment.
     *
     * @param id the id of the orderShipmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-shipments/{id}")
    public ResponseEntity<Void> deleteOrderShipment(@PathVariable Long id) {
        log.debug("REST request to delete OrderShipment : {}", id);
        orderShipmentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
