package com.badals.admin.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.admin.domain.ShipmentItem} entity.
 */
public class ShipmentItemDTO implements Serializable {

    private Long id;

    private Integer sequence;

    private BigDecimal quantity;

    private String description;


    private Long shipmentId;

    private Long productId;

    public ShipmentItemDTO(Integer sequence, BigDecimal quantity, String description, Long shipmentId, Long productId) {
        this.sequence = sequence;
        this.quantity = quantity;
        this.description = description;
        this.shipmentId = shipmentId;
        this.productId = productId;
    }

    public ShipmentItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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
