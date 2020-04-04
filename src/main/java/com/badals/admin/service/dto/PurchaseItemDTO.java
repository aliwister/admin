package com.badals.admin.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.PurchaseItem} entity.
 */
@Data
public class PurchaseItemDTO implements Serializable {

    private Long id;

    private Integer sequence;

    private BigDecimal quantity;

    private BigDecimal price;

    private LocalDate estimatedDeliveryDate;

    private String shippingInstructions;

    private String description;

    private String comment;

    private Long purchaseId;

    private Long orderItemId;

    private Long orderId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PurchaseItemDTO purchaseItemDTO = (PurchaseItemDTO) o;
        if (purchaseItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseItemDTO{" +
            "id=" + getId() +
            ", sequence=" + getSequence() +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", estimatedDeliveryDate='" + getEstimatedDeliveryDate() + "'" +
            ", shippingInstructions='" + getShippingInstructions() + "'" +
            ", description='" + getDescription() + "'" +
            ", comment='" + getComment() + "'" +
            ", purchase=" + getPurchaseId() +
            "}";
    }
}
