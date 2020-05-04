package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.service.dto.ShipmentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Shipment} and its DTO {@link ShipmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, CustomerMapper.class, MerchantMapper.class, PkgMapper.class, ShipmentItemMapper.class})
public interface ShipmentMapper extends EntityMapper<ShipmentDTO, Shipment> {

    //@Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.firstname", target = "customerFirstName")
    @Mapping(source = "customer.lastname", target = "customerLastName")
    @Mapping(source = "merchant.name", target = "merchantName")
    @Mapping(source = "merchant.id", target = "merchantId")
    /*@Mapping(source = "shipmentProgress.total", target = "progressTotal")
    @Mapping(source = "shipmentProgress.done", target = "progressDone")
    @Mapping(source = "shipmentProgress.todo", target = "progressTodo")*/
    ShipmentDTO toDto(Shipment shipment);

    @Named(value = "list")
    //@Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.firstname", target = "customerFirstName")
    @Mapping(source = "customer.lastname", target = "customerLastName")
    @Mapping(source = "merchant.name", target = "merchantName")
    @Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(target = "pkgs", ignore = true)
    @Mapping(target = "shipmentItems", ignore = true)
    /*@Mapping(source = "shipmentProgress.total", target = "progressTotal")
    @Mapping(source = "shipmentProgress.done", target = "progressDone")
    @Mapping(source = "shipmentProgress.todo", target = "progressTodo")*/
    ShipmentDTO toDtoList(Shipment shipment);

    //@Mapping(source = "addressId", target = "address")
    @Mapping(target = "shipmentItems", ignore = true)
    @Mapping(target = "pkgs", ignore = true)
    @Mapping(target = "removeShipmentItem", ignore = true)
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "merchantId", target = "merchant")
    Shipment toEntity(ShipmentDTO shipmentDTO);

    default Shipment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Shipment shipment = new Shipment();
        shipment.setId(id);
        return shipment;
    }
}
