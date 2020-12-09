package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.service.dto.PkgDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pkg} and its DTO {@link PkgDTO}.
 */
@Mapper(componentModel = "spring", uses = {ShipmentMapper.class, ShipmentItemMapper.class})
public interface PkgMapper extends EntityMapper<PkgDTO, Pkg> {

    @Mapping(source = "shipment.id", target = "shipmentId")
    @Mapping(target = "shipmentItems", ignore = true)
    PkgDTO toDto(Pkg pkg);

    @Mapping(source = "shipmentId", target = "shipment")
    Pkg toEntity(PkgDTO pkgDTO);

    default Pkg fromId(Long id) {
        if (id == null) {
            return null;
        }
        Pkg pkg = new Pkg();
        pkg.setId(id);
        return pkg;
    }
}
