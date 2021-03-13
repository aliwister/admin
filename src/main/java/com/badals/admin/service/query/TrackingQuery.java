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
        authorizeUser();
        return trackingService.shipmentList(listName);
    }

    public void authorizeUser() throws IllegalAccessException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getAuthorities().stream().anyMatch(t->t.getAuthority().equals("ROLE_ADMIN")))
            return;
        throw new IllegalAccessException("Not Authorized");
    }

    public List<ItemTrackingPojo> advancedTracking(String ref, boolean showAll) {
        return trackingService.trackByItem(ref, showAll?1:0);
    }
}

