package com.badals.admin.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.admin.domain.PurchaseShipment} entity.
 */
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Long getShipmentItemId() {
        return shipmentItemId;
    }

    public void setShipmentItemId(Long shipmentItemId) {
        this.shipmentItemId = shipmentItemId;
    }

    public Long getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemId(Long purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }

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
