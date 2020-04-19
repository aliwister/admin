package com.badals.admin.service.query;


import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.projection.Inventory;
import com.badals.admin.domain.projection.SortQueue;
import com.badals.admin.service.ShipmentService;
import com.badals.admin.service.dto.ShipmentDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShipmentQuery implements GraphQLQueryResolver {

    @Autowired
    ShipmentService shipmentService;


    public List<ShipmentDTO> shipments(List<ShipmentStatus> status, ShipmentType type) {
        return shipmentService.findForShipmentList(status, type);
    }

    public ShipmentDTO shipment(Long id) {
        return shipmentService.findOne(id).get();
    }

    public List<SortQueue> sortQueue(String keyword) {
        return shipmentService.findForSorting(keyword);
    }

    public List<Inventory> inventory() {
        return shipmentService.getInventory();
    }
}

