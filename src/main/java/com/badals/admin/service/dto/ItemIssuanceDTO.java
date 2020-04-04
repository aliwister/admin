package com.badals.admin.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.admin.domain.ItemIssuance} entity.
 */
public class ItemIssuanceDTO implements Serializable {

    private Long id;

    private BigDecimal quantity;


    private Long productId;

    private Long shipmentItemId;

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getShipmentItemId() {
        return shipmentItemId;
    }

    public void setShipmentItemId(Long shipmentItemId) {
        this.shipmentItemId = shipmentItemId;
    }

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
