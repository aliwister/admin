package com.badals.admin.service.dto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.admin.domain.ItemIssuance} entity.
 */
@Data
public class ItemIssuanceDTO implements Serializable {

    private Long id;

    private BigDecimal quantity;

    private Long shipmentId;

    private Long productId;

    private Long shipmentItemId;



    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ItemIssuanceDTO itemIssuanceDTO = (ItemIssuanceDTO) o;
        if (itemIssuanceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), itemIssuanceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ItemIssuanceDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", product=" + getProductId() +
            ", shipmentItem=" + getShipmentItemId() +
            "}";
    }
}
