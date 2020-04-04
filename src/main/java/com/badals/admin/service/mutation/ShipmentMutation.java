package com.badals.admin.service.mutation;

import com.badals.admin.domain.PackagingContent;
import com.badals.admin.domain.ShipmentItem;
import com.badals.admin.service.PackagingContentService;
import com.badals.admin.service.PkgService;
import com.badals.admin.service.PurchaseShipmentService;
import com.badals.admin.service.ShipmentService;
import com.badals.admin.service.dto.*;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ShipmentMutation implements GraphQLMutationResolver {
    @Autowired
    ShipmentService shipmentService;

    @Autowired
    PurchaseShipmentService purchaseShipmentService;

    @Autowired
    PkgService pkgService;

    @Autowired
    PackagingContentService packagingContentService;

    ShipmentDTO acceptShipment(ShipmentDTO dto) {
        return shipmentService.save(dto);
    }

    ShipmentDTO saveShipment(ShipmentDTO dto) {
        return shipmentService.save(dto);
    }

    PkgDTO acceptPackage(PkgDTO dto) {
        return pkgService.save(dto);
    }

    Message acceptItem(Long shipmentId, Long pkgId, Long purchaseItemId, Long productId, Long merchantId, String description, BigDecimal quantity, BigDecimal accepted, BigDecimal rejected)  {
        shipmentService.acceptItem(shipmentId, pkgId, purchaseItemId, productId, merchantId, description, quantity, accepted, rejected);
        return new Message("Done");
    }

    Message issueItem(Long orderItemId, Long productId, String description, BigDecimal quantity) {
        shipmentService.issueItem(orderItemId, productId, description, quantity);
        return new Message("Done");
    }

    Message prepItem(PackagingContentDTO dto) {
        packagingContentService.save(dto);
        return new Message("Done");
    }
    public Message sendToDetrack(Long shipmentId, Long orderId, String name, String instructions, String date, String time, String assignTo) throws JsonProcessingException {
        String m = shipmentService.sendToDetrack(shipmentId, orderId, name, instructions, date, time, assignTo);
        return new Message(m);
    }
}

