package com.badals.admin.service.mutation;

import com.badals.admin.service.*;
import com.badals.admin.service.dto.*;

import com.badals.admin.service.errors.ShipmentNotReadyException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Component
public class ShipmentMutation implements GraphQLMutationResolver {
    @Autowired
    ShipmentService shipmentService;

    @Autowired
    TrackingService trackingService;

    @Autowired
    PurchaseShipmentService purchaseShipmentService;

    @Autowired
    PkgService pkgService;

    @Autowired
    PackagingContentService packagingContentService;

    ShipmentDTO createShipment(ShipmentDTO dto) {
        return shipmentService.save(dto);
    }

    ShipmentDTO saveShipment(ShipmentDTO dto) {
        return shipmentService.save(dto);
    }

    PkgDTO acceptPackage(PkgDTO dto) {
        return pkgService.save(dto);
    }

    Message acceptItem(Long shipmentItemId, Long packageId, BigDecimal accepted, BigDecimal rejected)  {
        shipmentService.acceptItem(shipmentItemId, packageId, accepted, rejected);
        return new Message("Done");
    }

    ItemIssuanceDTO issueItem(Long orderItemId, Long productId, String description, BigDecimal quantity) throws Exception {
        return shipmentService.issueItem(orderItemId, productId, description, quantity);
        //return new Message("Done");
    }

    Message prepItem(PackagingContentDTO dto) {
        packagingContentService.save(dto);
        return new Message("Done");
    }

    Message addItem(Long shipmentId, Long productId, Long purchaseItemId, String description, BigDecimal quantity)
     {
        String m = shipmentService.addItem(shipmentId,
            productId,
            purchaseItemId,
            description,
            quantity);
        return new Message("Done");
    }

    public Message sendToDetrack(Long shipmentId, String orderId, String name, String instructions, String date, String time, String assignTo) throws JsonProcessingException, ShipmentNotReadyException {
        String m = shipmentService.sendToDetrack(shipmentId, orderId, name, instructions, date, time, assignTo);
        return new Message(m);
    }

    public Message processAmazonShipments() throws IOException {
        return trackingService.processAmazonFile();
    }
    public ShipmentDTO createShipment(ShipmentDTO shipment, List<ShipmentItemDTO> shipmentItems) throws Exception {
        return trackingService.createShipment(shipment, shipmentItems);
    }

    public ShipmentDTO acceptShipment(String trackingNum) throws IOException {
        return shipmentService.acceptShipment(trackingNum);
    }
}

