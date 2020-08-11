package com.badals.admin.service.query;


import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;

import com.badals.admin.domain.pojo.ShipmentItemSummaryImpl;
import com.badals.admin.domain.pojo.ShipmentTrackingMap;
import com.badals.admin.domain.pojo.TrackingEvent;
import com.badals.admin.domain.projection.*;
import com.badals.admin.service.ShipmentService;
import com.badals.admin.service.TrackingService;
import com.badals.admin.service.dto.ShipmentDTO;
import com.badals.admin.service.dto.ShipmentItemDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShipmentQuery extends AdminQuery implements GraphQLQueryResolver {
    private final ShipmentService shipmentService;
    private final TrackingService trackingService;

    public ShipmentQuery(ShipmentService shipmentService, TrackingService trackingService) {
        this.shipmentService = shipmentService;
        this.trackingService = trackingService;
    }

    public List<ShipmentDTO> shipments(List<ShipmentStatus> status, ShipmentType type) throws IllegalAccessException {
        authorizeUser();
        return shipmentService.findForShipmentList(status, type);
    }

    public ShipmentDTO shipment(Long id) throws IllegalAccessException {
        authorizeUser();
        return shipmentService.findOne(id).get();
    }

    public List<ShipmentDTO> shipmentsByRef(String ref) throws IllegalAccessException {
        authorizeUser();
        return shipmentService.findByRef(ref);
    }

    public List<SortQueue> sortQueue(String keyword) throws IllegalAccessException {
        authorizeUser();
        return shipmentService.findForSorting(keyword);
    }

    public List<OutstandingQueue> outstandingQueue(String keyword) throws IllegalAccessException {
        authorizeUser();
        return shipmentService.findOutstanding(keyword);
    }
    public List<UnshippedQueue> unshippedQueue() throws IllegalAccessException {
        //authorizeUser();
        return shipmentService.findUnshipped();
    }
    public List<Inventory> inventory() throws IllegalAccessException {
        authorizeUser();
        return shipmentService.getInventory();
    }

    public List<ShipQueue> shipQueue() throws IllegalAccessException {
        authorizeUser();
        return shipmentService.getShipQueue();
    }

    public List<PrepQueue> prepQueue(Long shipmentId, String keyword) throws IllegalAccessException {
        authorizeUser();
        return shipmentService.getPrepQueue(shipmentId, keyword);
    }


    public List<ShipmentItemDTO> shipmentItemsByTrackingNums(List<String> trackingNums) throws IllegalAccessException {
        authorizeUser();
        return trackingService.findByTrackingNums(trackingNums);
    }


    public List<ShipmentItemSummaryImpl> shipmentItemsCountByTrackingNums(List<String> trackingNums) throws IllegalAccessException {
        authorizeUser();
        return trackingService.findCountByTrackingNums(trackingNums);
    }

    public List<TrackingEvent> trackingEvents() throws IllegalAccessException {
        authorizeUser();
        return trackingService.trackingEvents();
    }

    public void authorizeUser() throws IllegalAccessException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAuthorities().stream().anyMatch(t->t.getAuthority().equals("ROLE_ADMIN")))
            return;
        throw new IllegalAccessException("Not Authorized");
    }



}

