package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.service.dto.PurchaseShipmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseShipment} and its DTO {@link PurchaseShipmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {ShipmentItemMapper.class, PurchaseItemMapper.class})
public interface PurchaseShipmentMapper extends EntityMapper<PurchaseShipmentDTO, PurchaseShipment> {

    @Mapping(source = "shipmentItem.id", target = "shipmentItemId")
    @Mapping(source = "purchaseItem.id", target = "purchaseItemId")
    PurchaseShipmentDTO toDto(PurchaseShipment purchaseShipment);

    @Mapping(source = "shipmentItemId", target = "shipmentItem")
    @Mapping(source = "purchaseItemId", target = "purchaseItem")
    PurchaseShipment toEntity(PurchaseShipmentDTO purchaseShipmentDTO);

    default PurchaseShipment fromId(Long id) {
        if (id == null) {
            return null;
        }
        PurchaseShipment purchaseShipment = new PurchaseShipment();
        purchaseShipment.setId(id);
        return purchaseShipment;
    }
}
