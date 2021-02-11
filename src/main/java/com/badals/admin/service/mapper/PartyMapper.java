package com.badals.admin.service.mapper;

import com.badals.admin.domain.Party;
import com.badals.admin.service.dto.PartyDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Party} and its DTO {@link PartyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PartyMapper extends EntityMapper<PartyDTO, Party> {



    default Party fromId(String id) {
        if (id == null) {
            return null;
        }
        Party customer = new Party();
        customer.setId(id);
        return customer;
    }
}
