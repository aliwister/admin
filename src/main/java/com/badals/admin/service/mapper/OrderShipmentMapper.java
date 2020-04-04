package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.service.dto.OrderShipmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderShipment} and its DTO {@link OrderShipmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {/*OrderItemMapper.class,*/ ShipmentItemMapper.class})
public interface OrderShipmentMapper extends EntityMapper<OrderShipmentDTO, OrderShipment> {

    //@Mapping(source = "orderItem.id", target = "orderItemId")
    @Mapping(source = "shipmentItem.id", target = "shipmentItemId")
    OrderShipmentDTO toDto(OrderShipment orderShipment);

    //@Mapping(source = "orderItemId", target = "orderItem")
    @Mapping(source = "shipmentItemId", target = "shipmentItem")
    OrderShipment toEntity(OrderShipmentDTO orderShipmentDTO);

    default OrderShipment fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderShipment orderShipment = new OrderShipment();
        orderShipment.setId(id);
        return orderShipment;
    }
}
