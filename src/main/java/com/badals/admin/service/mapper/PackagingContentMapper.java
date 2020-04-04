package com.badals.admin.service.mapper;

import com.badals.admin.domain.*;
import com.badals.admin.service.dto.PackagingContentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PackagingContent} and its DTO {@link PackagingContentDTO}.
 */
@Mapper(componentModel = "spring", uses = {PkgMapper.class, ShipmentItemMapper.class})
public interface PackagingContentMapper extends EntityMapper<PackagingContentDTO, PackagingContent> {

    @Mapping(source = "pkg.id", target = "pkgId")
    @Mapping(source = "shipmentItem.id", target = "shipmentItemId")
    PackagingContentDTO toDto(PackagingContent packagingContent);

    @Mapping(source = "pkgId", target = "pkg")
    @Mapping(source = "shipmentItemId", target = "shipmentItem")
    PackagingContent toEntity(PackagingContentDTO packagingContentDTO);

    default PackagingContent fromId(Long id) {
        if (id == null) {
            return null;
        }
        PackagingContent packagingContent = new PackagingContent();
        packagingContent.setId(id);
        return packagingContent;
    }
}
