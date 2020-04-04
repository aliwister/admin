package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.service.dto.ShipmentDocDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShipmentDoc} and its DTO {@link ShipmentDocDTO}.
 */
@Mapper(componentModel = "spring", uses = {ShipmentMapper.class})
public interface ShipmentDocMapper extends EntityMapper<ShipmentDocDTO, ShipmentDoc> {

    @Mapping(source = "shipment.id", target = "shipmentId")
    ShipmentDocDTO toDto(ShipmentDoc shipmentDoc);

    @Mapping(source = "shipmentId", target = "shipment")
    ShipmentDoc toEntity(ShipmentDocDTO shipmentDocDTO);

    default ShipmentDoc fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShipmentDoc shipmentDoc = new ShipmentDoc();
        shipmentDoc.setId(id);
        return shipmentDoc;
    }
}
