package com.badals.admin.service.mutation;

import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.service.*;
import com.badals.admin.service.dto.*;

import com.badals.admin.service.errors.ShipmentNotReadyException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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


    ShipmentDTO createShipment(ShipmentDTO dto) throws IllegalAccessException {
        authorizeUser();
        return shipmentService.save(dto);
    }

    ShipmentDTO saveShipment(ShipmentDTO dto) throws IllegalAccessException {
        authorizeUser();
    return shipmentService.save(dto);
    }

    PkgDTO acceptPackage(PkgDTO dto) throws IllegalAccessException {
        authorizeUser();
        return pkgService.save(dto);
    }

    Message acceptItem(Long shipmentItemId, Long packageId, BigDecimal accepted, BigDecimal rejected) throws IllegalAccessException {
        authorizeUser();
        shipmentService.acceptItem(shipmentItemId, packageId, accepted, rejected);
        return new Message("Done");
    }

    ItemIssuanceDTO issueItem(Long orderItemId, Long productId, String description, BigDecimal quantity) throws Exception {
        authorizeUser();
        return shipmentService.issueItem(orderItemId, productId, description, quantity);
        //return new Message("Done");
    }

    Message prepItem(PackagingContentDTO dto) throws IllegalAccessException {
        authorizeUser();
        packagingContentService.save(dto);
        return new Message("Done");
    }

    Message addItem(Long shipmentId, Long productId, Long purchaseItemId, String description, BigDecimal quantity) throws IllegalAccessException {
         authorizeUser();
        String m = shipmentService.addItem(shipmentId,
            productId,
            purchaseItemId,
            description,
            quantity);
        return new Message("Done");
    }

    public Message sendToDetrack(Long shipmentId, String orderId, String name, String instructions, String date, String time, String assignTo) throws JsonProcessingException, ShipmentNotReadyException, IllegalAccessException {
        authorizeUser();
        String m = shipmentService.sendToDetrack(shipmentId, orderId, name, instructions, date, time, assignTo);
        return new Message(m);
    }

    public Message processAmazonShipments() throws IOException, IllegalAccessException {
        authorizeUser();
        return trackingService.processAmazonFile();
    }
    public ShipmentDTO createShipment(ShipmentDTO shipment, List<ShipmentItemDTO> shipmentItems, List<String> trackingNums) throws Exception {
        authorizeUser();
        return trackingService.createShipment(shipment, shipmentItems, trackingNums);
    }

    public ShipmentDTO acceptShipment(String trackingNum) throws IOException, IllegalAccessException {
        authorizeUser();
        return shipmentService.acceptShipment(trackingNum);
    }

    public Message addTrackingEvent(List<String> trackingNums, ShipmentStatus shipmentStatus, Integer trackingEvent, LocalDateTime eventDate, String details) throws IllegalAccessException {
        authorizeUser();
        return trackingService.addTrackingMulti(trackingNums, shipmentStatus, trackingEvent, eventDate, details);
    }
    public void authorizeUser() throws IllegalAccessException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAuthorities().stream().anyMatch(t->t.getAuthority().equals("ROLE_ADMIN")))
            return;
        throw new IllegalAccessException("Not Authorized");
    }

    public Message setShipmentStatus(Long shipmentId, ShipmentStatus status) throws IllegalAccessException {
        authorizeUser();
        shipmentService.setStatus(shipmentId, status);
        return new Message("Success");
    }
}

