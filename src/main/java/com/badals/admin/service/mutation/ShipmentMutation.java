package com.badals.admin.service.mutation;

import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.pojo.PaymentPojo;
import com.badals.admin.service.*;
import com.badals.admin.service.dto.*;

import com.badals.admin.service.errors.ShipmentNotReadyException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.fasterxml.jackson.core.JsonProcessingException;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Date;
import java.util.List;


/*import static org.jeasy.flows.workflow.ParallelFlow.Builder.aNewParallelFlow;
import static org.jeasy.flows.workflow.RepeatFlow.Builder.aNewRepeatFlow;*/

@Component
public class ShipmentMutation implements GraphQLMutationResolver {

    private final ShipmentService shipmentService;
    private final TrackingService trackingService;
    private final PurchaseShipmentService purchaseShipmentService;
    private final PkgService pkgService;
    private final PackagingContentService packagingContentService;

    public ShipmentMutation(ShipmentService shipmentService, TrackingService trackingService, PurchaseShipmentService purchaseShipmentService, PkgService pkgService, PackagingContentService packagingContentService) {
        this.shipmentService = shipmentService;
        this.trackingService = trackingService;
        this.purchaseShipmentService = purchaseShipmentService;
        this.pkgService = pkgService;
        this.packagingContentService = packagingContentService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ShipmentDTO createShipment(ShipmentDTO dto) throws IllegalAccessException {
        return shipmentService.save(dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ShipmentDTO saveShipment(ShipmentDTO dto) throws IllegalAccessException {
        return shipmentService.saveShallow(dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    PkgDTO acceptPackage(PkgDTO dto) throws IllegalAccessException {
        return pkgService.save(dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Message acceptItem(Long shipmentItemId, Long packageId, BigDecimal accepted, BigDecimal rejected) throws IllegalAccessException, ShipmentNotReadyException {
        shipmentService.acceptItem(shipmentItemId, packageId, accepted, rejected);
        return new Message("Done");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    ItemIssuanceDTO issueItem(Long orderItemId, Long productId, String description, BigDecimal quantity) throws Exception {
        return shipmentService.issueItem(orderItemId, productId, description, quantity);
        //return new Message("Done");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Message prepItem(PackagingContentDTO dto) throws IllegalAccessException {
        packagingContentService.save(dto);
        return new Message("Done");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Message addItem(Long shipmentId, Long productId, Long purchaseItemId, String description, BigDecimal quantity) throws IllegalAccessException {
        String m = shipmentService.addItem(shipmentId,
            productId,
            purchaseItemId,
            description,
            quantity);
        return new Message("Done");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message sendToDetrack(Long shipmentId, String orderId, String name, String instructions, String date, String time, String assignTo) throws JsonProcessingException, ShipmentNotReadyException, IllegalAccessException {
        String m = shipmentService.sendToDetrack(shipmentId, orderId, name, instructions, date, time, assignTo);
        return new Message(m);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ShipmentDTO createShipment(ShipmentDTO shipment, List<ShipmentItemDTO> shipmentItems, List<String> trackingNums) throws Exception {
        return trackingService.createShipment(shipment, shipmentItems, trackingNums);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ShipmentDTO acceptShipment(String trackingNum, PaymentPojo payment, String invoiceLink) throws Exception {
        return shipmentService.acceptShipment(trackingNum, payment, invoiceLink);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message addTrackingEvent(List<String> trackingNums, ShipmentStatus shipmentStatus, Integer trackingEvent, LocalDateTime eventDate, String details) throws IllegalAccessException {
        return trackingService.addTrackingMulti(trackingNums, shipmentStatus, trackingEvent, eventDate, details);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message setShipmentStatus(Long shipmentId, ShipmentStatus status) throws IllegalAccessException {
        shipmentService.setStatus(shipmentId, status);
        return new Message("Success");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message removeItem(Long shipmentId, Long shipmentItemId, String description, BigDecimal quantity) throws IllegalAccessException {
        shipmentService.removeItem(shipmentItemId);
        return new Message("Success");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message unpackItem(Long shipmentId, Long shipmentItemId, String description, BigDecimal quantity) throws IllegalAccessException {
        shipmentService.unpackItem(shipmentItemId);
        return new Message("Success");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message updateFromDetrack(String id) throws IOException {
        shipmentService.updateFromDetrack(id);
        return new Message("Success");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message setEstimatedShipDate(Long id, Date date) {
        shipmentService.setEstimatedShipDate(id, date);
        return new Message("Success");
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public Message addShipmentDoc(Long id, String filename) {
        shipmentService.addShipmentDoc(id, filename);
        return new Message("Success");
    }

}
