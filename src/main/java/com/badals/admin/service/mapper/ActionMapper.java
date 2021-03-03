package com.badals.admin.service.mapper;

import com.badals.admin.service.dto.ActionDTO;
import com.badals.admin.domain.Action;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ActionMapper extends EntityMapper<ActionDTO, Action> {



    default Action fromId(Long id) {
        if (id == null) {
            return null;
        }
        Action action = new Action();
        action.setId(id);
        return action;
    }
}
