package com.badals.admin.service.mapper;


import com.badals.admin.domain.Merchant;
import com.badals.admin.service.dto.MerchantDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Merchant} and its DTO {@link MerchantDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MerchantMapper extends EntityMapper<MerchantDTO, Merchant> {



    default Merchant fromId(Long id) {
        if (id == null) {
            return null;
        }
        Merchant merchant = new Merchant();
        merchant.setId(id);
        return merchant;
    }
}
