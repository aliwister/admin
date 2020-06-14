package com.badals.admin.service.dto;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.badals.admin.domain.ShipmentItem} entity.
 */

@Data
public class ShipmentItemDTO implements Serializable {

    private Long id;

    private Integer sequence;

    private BigDecimal quantity;

    private String description;


    private Long shipmentId;

    private Long productId;
    private Long productRef;

    private String image;

    private Long from;

    private Set<PurchaseShipmentDTO> purchaseShipments;

    public ShipmentItemDTO(Integer sequence, BigDecimal quantity, String description, Long shipmentId, Long productId) {
        this.sequence = sequence;
        this.quantity = quantity;
        this.description = description;
        this.shipmentId = shipmentId;
        this.productId = productId;
    }

    public ShipmentItemDTO() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShipmentItemDTO shipmentItemDTO = (ShipmentItemDTO) o;
        if (shipmentItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shipmentItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShipmentItemDTO{" +
            "id=" + getId() +
            ", sequence=" + getSequence() +
            ", quantity=" + getQuantity() +
            ", description='" + getDescription() + "'" +
            ", shipment=" + getShipmentId() +
            ", product=" + getProductId() +
            "}";
    }
}
