package com.badals.admin.service.mapper;

import com.badals.admin.domain.Order;
import com.badals.admin.service.dto.AddressDTO;
import com.badals.admin.service.dto.OrderDTO;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, AddressMapper.class})
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "removeOrderItem", ignore = true)
    Order toEntity(OrderDTO orderDTO);

    //@Mapping(target="cart", ignore = true)
    OrderDTO toDto(Order order);

    default Order fromId(Long id) {
        if (id == null) {
            return null;
        }
        Order order = new Order();
        order.setId(id);
        return order;
    }

    @AfterMapping
    default void afterMapping(@MappingTarget OrderDTO target, Order source) {
        if(target.getDeliveryAddress() ==  null)
            target.setDeliveryAddress(AddressDTO.fromAddressPojo(source.getDeliveryAddressPojo()));
    }
}
