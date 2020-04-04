package com.badals.admin.service.mapper;

import com.badals.admin.domain.PurchaseItem;
import com.badals.admin.service.dto.PurchaseItemDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link PurchaseItem} and its DTO {@link PurchaseItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {/*PurchaseMapper.class, OrderItemMapper.class*/})
public interface PurchaseItemMapper extends EntityMapper<PurchaseItemDTO, PurchaseItem> {

    //@Mapping(source = "purchase.id", target = "purchaseId")
    //@Mapping(source = "orderItem.id", target = "orderItemId")
    //@Mapping(source = "orderItem.order.id", target = "orderId")
    PurchaseItemDTO toDto(PurchaseItem purchaseItem);

    //@Mapping(source = "purchaseId", target = "purchase")
   // @Mapping(source = "orderItemId", target = "orderItem")
    PurchaseItem toEntity(PurchaseItemDTO purchaseItemDTO);

    default PurchaseItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        PurchaseItem purchaseItem = new PurchaseItem();
        purchaseItem.setId(id);
        return purchaseItem;
    }
}
