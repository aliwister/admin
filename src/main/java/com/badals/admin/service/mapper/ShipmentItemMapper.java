package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.service.dto.ShipmentItemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShipmentItem} and its DTO {@link ShipmentItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {ShipmentMapper.class, PurchaseShipmentMapper.class})
public interface ShipmentItemMapper extends EntityMapper<ShipmentItemDTO, ShipmentItem> {

    @Mapping(source = "shipment.id", target = "shipmentId")
    @Mapping(source = "product.ref", target = "productId")
    @Mapping(source = "product.image", target = "image")
    @Mapping(source = "product.price", target = "price")
    ShipmentItemDTO toDto(ShipmentItem shipmentItem);

    @Mapping(source = "shipmentId", target = "shipment")
    @Mapping(target = "purchaseShipments", ignore = true)
    //@Mapping(source = "productId", target = "productId")
    ShipmentItem toEntity(ShipmentItemDTO shipmentItemDTO);

    default ShipmentItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShipmentItem shipmentItem = new ShipmentItem();
        shipmentItem.setId(id);
        return shipmentItem;
    }
}
