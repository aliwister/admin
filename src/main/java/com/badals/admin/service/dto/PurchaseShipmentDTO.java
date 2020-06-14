package com.badals.admin.service.dto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.admin.domain.PurchaseShipment} entity.
 */
@Data
public class PurchaseShipmentDTO implements Serializable {

    private Long id;

    private BigDecimal quantity;


    private Long shipmentItemId;

    private Long purchaseItemId;

    public PurchaseShipmentDTO() {
    }

    public PurchaseShipmentDTO(BigDecimal quantity, Long shipmentItemId, Long purchaseItemId) {
        this.quantity = quantity;
        this.shipmentItemId = shipmentItemId;
        this.purchaseItemId = purchaseItemId;
    }

    String __typename;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PurchaseShipmentDTO purchaseShipmentDTO = (PurchaseShipmentDTO) o;
        if (purchaseShipmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseShipmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseShipmentDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", shipmentItem=" + getShipmentItemId() +
            ", purchaseItem=" + getPurchaseItemId() +
            "}";
    }
}
