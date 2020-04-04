package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.service.dto.ItemIssuanceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemIssuance} and its DTO {@link ItemIssuanceDTO}.
 */
@Mapper(componentModel = "spring", uses = {/*ProductMapper.class, */ShipmentItemMapper.class})
public interface ItemIssuanceMapper extends EntityMapper<ItemIssuanceDTO, ItemIssuance> {

    //@Mapping(source = "product.id", target = "productId")
    @Mapping(source = "shipmentItem.id", target = "shipmentItemId")
    ItemIssuanceDTO toDto(ItemIssuance itemIssuance);

    //@Mapping(source = "productId", target = "product")
    @Mapping(source = "shipmentItemId", target = "shipmentItem")
    ItemIssuance toEntity(ItemIssuanceDTO itemIssuanceDTO);

    default ItemIssuance fromId(Long id) {
        if (id == null) {
            return null;
        }
        ItemIssuance itemIssuance = new ItemIssuance();
        itemIssuance.setId(id);
        return itemIssuance;
    }
}
