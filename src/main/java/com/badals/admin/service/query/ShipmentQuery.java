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
import com.badals.admin.service.dto.ShipmentDocDTO;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ShipmentDTO> shipments(List<ShipmentStatus> status, ShipmentType type) throws IllegalAccessException {

        return shipmentService.findForShipmentList(status, type);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ShipmentDTO shipment(Long id) throws IllegalAccessException {

        return shipmentService.findOneForDetails(id).get();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ShipmentItemDetails> shipmentItemDetails(Long id) throws IllegalAccessException {

        return shipmentService.findItemsForShipmentDetails(id);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ShipmentItemDetails> pkgItemDetails(Long id) throws IllegalAccessException {

        return shipmentService.findItemsInPkgForShipmentDetails(id);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ShipmentDTO> shipmentsByRef(String ref) throws IllegalAccessException {

        return shipmentService.findByRef(ref);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<SortQueue> sortQueue(String keyword) throws IllegalAccessException {

        return shipmentService.findForSorting(keyword);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<OutstandingQueue> outstandingQueue(String keyword) throws IllegalAccessException {

        return shipmentService.findOutstanding(keyword);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UnshippedQueue> unshippedQueue() throws IllegalAccessException {
        //authorizeUser();
        return shipmentService.findUnshipped();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Inventory> inventory() throws IllegalAccessException {

        return shipmentService.getInventory();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ShipQueue> shipQueue() throws IllegalAccessException {

        return shipmentService.getShipQueue();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ShipQueue> getShipQueueByCustomerId(Long customerId) throws IllegalAccessException {

        return shipmentService.getShipQueueByCustomerId(customerId);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PrepQueue> prepQueue(Long shipmentId, String keyword) throws IllegalAccessException {

        return shipmentService.getPrepQueue(shipmentId, keyword);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ShipmentItemDTO> shipmentItemsByTrackingNums(List<String> trackingNums, boolean isClosed) throws IllegalAccessException {

        return trackingService.findByTrackingNums(trackingNums, isClosed);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ShipmentItemSummaryImpl> shipmentItemsCountByTrackingNums(List<String> trackingNums) throws IllegalAccessException {

        return trackingService.findCountByTrackingNums(trackingNums);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TrackingEvent> trackingEvents() throws IllegalAccessException {

        return trackingService.trackingEvents();
    }

    public List<ShipmentDocDTO> shipmentDocs(Long id) {
        return shipmentService.shipmentDocs(id);
    }
}

