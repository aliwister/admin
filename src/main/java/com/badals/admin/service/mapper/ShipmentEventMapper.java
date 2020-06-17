package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.service.dto.ShipmentEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShipmentEvent} and its DTO {@link ShipmentEventDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ShipmentEventMapper extends EntityMapper<ShipmentEventDTO, ShipmentEvent> {



    default ShipmentEvent fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShipmentEvent shipmentEvent = new ShipmentEvent();
        shipmentEvent.setId(id);
        return shipmentEvent;
    }
}
