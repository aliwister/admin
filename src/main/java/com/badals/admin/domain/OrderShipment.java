package com.badals.admin.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A OrderShipment.
 */
@Entity
@Table(name = "order_shipment")
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "ordershipment")
public class OrderShipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

    /*@ManyToOne
    private OrderItem orderItem;
    */
    //@ManyToOne

    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne
    private ShipmentItem shipmentItem;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public OrderShipment quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
/*
    public OrderItem getOrderItem() {
        return orderItem;
    }

    public OrderShipment orderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
        return this;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }*/

    public ShipmentItem getShipmentItem() {
        return shipmentItem;
    }

    public OrderShipment shipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItem = shipmentItem;
        return this;
    }

    public void setShipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItem = shipmentItem;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderShipment)) {
            return false;
        }
        return id != null && id.equals(((OrderShipment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "OrderShipment{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
