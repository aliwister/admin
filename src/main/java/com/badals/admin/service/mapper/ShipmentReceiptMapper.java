package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.service.dto.ShipmentReceiptDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShipmentReceipt} and its DTO {@link ShipmentReceiptDTO}.
 */
@Mapper(componentModel = "spring", uses = {PkgMapper.class, ShipmentItemMapper.class/*, ProductMapper.class*/})
public interface ShipmentReceiptMapper extends EntityMapper<ShipmentReceiptDTO, ShipmentReceipt> {

    @Mapping(source = "pkg.id", target = "pkgId")
    @Mapping(source = "shipmentItem.id", target = "shipmentItemId")
/*    @Mapping(source = "product.id", target = "productId")*/
    ShipmentReceiptDTO toDto(ShipmentReceipt shipmentReceipt);

    @Mapping(source = "pkgId", target = "pkg")
    @Mapping(source = "shipmentItemId", target = "shipmentItem")
/*    @Mapping(source = "productId", target = "product")*/
    ShipmentReceipt toEntity(ShipmentReceiptDTO shipmentReceiptDTO);

    default ShipmentReceipt fromId(Long id) {
        if (id == null) {
            return null;
        }
        ShipmentReceipt shipmentReceipt = new ShipmentReceipt();
        shipmentReceipt.setId(id);
        return shipmentReceipt;
    }
}
