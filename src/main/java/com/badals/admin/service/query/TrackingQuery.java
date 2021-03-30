package com.badals.admin.service.query;


import com.badals.admin.domain.enumeration.ShipmentListView;
import com.badals.admin.domain.enumeration.ShipmentStatus;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.pojo.ItemTrackingPojo;
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
public class TrackingQuery extends AdminQuery implements GraphQLQueryResolver {
    private final ShipmentService shipmentService;
    private final TrackingService trackingService;



    public TrackingQuery(ShipmentService shipmentService, TrackingService trackingService) {
        this.shipmentService = shipmentService;
        this.trackingService = trackingService;
    }
    public List<ShipmentTrackingMap> track(String ref) {
        return trackingService.track(ref);
    }

    public List<ShipmentList> shipmentList(ShipmentListView listName) throws IllegalAccessException {
        return trackingService.shipmentList(listName);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ItemTrackingPojo> advancedTracking(String ref, boolean showAll, String queueName) {
        if(queueName != null)
            switch (queueName) {
                case "NO_PO":
                    return trackingService.trackByItem(ref, showAll?1:0,1,0,0, 0);
                case "PO_NO_TRANSIT":
                        return trackingService.trackByItem(ref, showAll?1:0,0,1,0, 0);
                case "LONG_TRANSIT":
                        return trackingService.trackByItem(ref, showAll?1:0,0,0,1, 0);
                case "LOST":
                        return trackingService.trackByItem(ref, showAll?1:0,0,0,0,1);
            }
        return trackingService.trackByItem(ref, showAll?1:0,0,0,0,0);
    }
}

