package com.badals.admin.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.admin.domain.OrderShipment} entity.
 */
public class OrderShipmentDTO implements Serializable {

    private Long id;

    private BigDecimal quantity;


    private Long orderItemId;

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

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
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

        OrderShipmentDTO orderShipmentDTO = (OrderShipmentDTO) o;
        if (orderShipmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderShipmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderShipmentDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", orderItem=" + getOrderItemId() +
            ", shipmentItem=" + getShipmentItemId() +
            "}";
    }
}
