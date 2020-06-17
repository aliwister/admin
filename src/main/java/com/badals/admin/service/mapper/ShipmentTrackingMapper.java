package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.service.dto.ShipmentTrackingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShipmentTracking} and its DTO {@link ShipmentTrackingDTO}.
 */
@Mapper(componentModel = "spring", uses = {ShipmentMapper.class, ShipmentEventMapper.class})
public interface ShipmentTrackingMapper extends EntityMapper<ShipmentTrackingDTO, ShipmentTracking> {

    @Mapping(source = "shipment.id", target = "shipmentId")
    @Mapping(source = "shipmentEvent.description", target = "shipmentEventDescription")
    @Mapping(source = "shipmentEvent.ref", target = "shipmentEventId")
    ShipmentTrackingDTO toDto(ShipmentTracking shipmentStatus);

    @Mapping(source = "shipmentId", target = "shipment")
    @Mapping(source = "shipmentEventId", target = "shipmentEvent")
    ShipmentTracking toEntity(ShipmentTrackingDTO shipmentStatusDTO);

    default ShipmentTracking fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShipmentTracking shipmentStatus = new ShipmentTracking();
        shipmentStatus.setId(id);
        return shipmentStatus;
    }
}
