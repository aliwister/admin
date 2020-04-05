package com.badals.admin.service.mapper;

import com.badals.admin.domain.Address;
import com.badals.admin.domain.pojo.AddressPojo;
import com.badals.admin.service.dto.AddressDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {

    @Mapping(source = "customer.id", target = "customerId")
    AddressDTO toDto(Address address);

    @Mapping(source = "customerId", target = "customer")
    Address toEntity(AddressDTO addressDTO);

    default Address fromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}
