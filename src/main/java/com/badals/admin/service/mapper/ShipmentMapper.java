package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.service.dto.OrderDTO;
import com.badals.admin.service.dto.ShipmentDTO;

import org.hibernate.annotations.Source;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Shipment} and its DTO {@link ShipmentDTO}.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, CustomerMapper.class, MerchantMapper.class, PkgMapper.class, ShipmentItemMapper.class, PartyMapper.class})
public interface ShipmentMapper extends EntityMapper<ShipmentDTO, Shipment> {

    //@Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.firstname", target = "customerFirstName")
    @Mapping(source = "customer.lastname", target = "customerLastName")
    @Mapping(source = "merchant.name", target = "merchantName")
    @Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(source = "to.name", target = "partyName")
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
    @Mapping(source = "to.name", target = "partyName")
    @Mapping(target = "pkgs", ignore = true)
    @Mapping(target = "shipmentItems", ignore = true)
    /*@Mapping(source = "shipmentProgress.total", target = "progressTotal")
    @Mapping(source = "shipmentProgress.done", target = "progressDone")
    @Mapping(source = "shipmentProgress.todo", target = "progressTodo")*/
    ShipmentDTO toDtoList(Shipment shipment);


    @Named(value = "details")
    //@Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.firstname", target = "customerFirstName")
    @Mapping(source = "customer.lastname", target = "customerLastName")
    @Mapping(source = "merchant.name", target = "merchantName")
    @Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(target = "shipmentItems", ignore = true)
    @Mapping(source = "to.name", target = "partyName")
    /*@Mapping(source = "shipmentProgress.total", target = "progressTotal")
    @Mapping(source = "shipmentProgress.done", target = "progressDone")
    @Mapping(source = "shipmentProgress.todo", target = "progressTodo")*/
    ShipmentDTO toDtoDetails(Shipment shipment);

    //@Mapping(source = "addressId", target = "address")
    @Mapping(target = "shipmentItems", ignore = true)
    @Mapping(target = "pkgs", ignore = true)
    @Mapping(target = "removeShipmentItem", ignore = true)
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "merchantId", target = "merchant")
    Shipment toEntity(ShipmentDTO shipmentDTO);

    @AfterMapping
    default void afterMapping(@MappingTarget Shipment target, ShipmentDTO source) {
        if(source.getPartyId() == null || source.getPartyId() <= 0L)
            return;
        String append = null;
        switch(target.getShipmentType()) {
            case PURCHASE:
                append = "m";
                break;
            case TRANSIT:
                append = "m";
                break;
            case CUSTOMER:
                append = "c";
                break;
        }
        if (append != null)
            target.setTo(new Party(append + source.getPartyId()));
    }

    @AfterMapping
    default void afterMapping(@MappingTarget ShipmentDTO target, Shipment source) {
        if(source.getTo() != null)
            target.setPartyId(Long.parseLong(source.getTo().getId().substring(1)));
    }



    default Shipment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Shipment shipment = new Shipment();
        shipment.setId(id);
        return shipment;
    }
}
