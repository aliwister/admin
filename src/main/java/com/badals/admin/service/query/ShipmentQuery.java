package com.badals.admin.service.query;


import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.pojo.ShipmentItemCountImpl;
import com.badals.admin.domain.projection.ShipmentItemCount;
import com.badals.admin.domain.projection.*;
import com.badals.admin.service.ShipmentService;
import com.badals.admin.service.TrackingService;
import com.badals.admin.service.dto.ShipmentDTO;
import com.badals.admin.service.dto.ShipmentItemDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShipmentQuery implements GraphQLQueryResolver {

    @Autowired
    ShipmentService shipmentService;

    @Autowired
    TrackingService trackingService;

    public List<ShipmentDTO> shipments(List<ShipmentStatus> status, ShipmentType type) {
        return shipmentService.findForShipmentList(status, type);
    }

    public ShipmentDTO shipment(Long id) {
        return shipmentService.findOne(id).get();
    }

    public List<ShipmentDTO> shipmentsByRef(String ref) {
        return shipmentService.findByRef(ref);
    }

    public List<SortQueue> sortQueue(String keyword) {
        return shipmentService.findForSorting(keyword);
    }

    public List<OutstandingQueue> outstandingQueue(String keyword) {
        return shipmentService.findOutstanding(keyword);
    }

    public List<Inventory> inventory() {
        return shipmentService.getInventory();
    }
    public List<ShipQueue> shipQueue() {
        return shipmentService.getShipQueue();
    }
    public List<PrepQueue> prepQueue(Long shipmentId, String keyword) {
        return shipmentService.getPrepQueue(shipmentId, keyword);
    }

    public List<ShipmentItemDTO> shipmentItemsByTrackingNums(List<String> trackingNums) {
        return trackingService.findByTrackingNums(trackingNums);
    }

    public List<ShipmentItemCountImpl> shipmentItemsCountByTrackingNums(List<String> trackingNums) {
        return trackingService.findCountByTrackingNums(trackingNums);
    }
}

