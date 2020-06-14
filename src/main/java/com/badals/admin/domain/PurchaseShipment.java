package com.badals.admin.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A PurchaseShipment.
 */
@Entity
@Table(name = "purchase_shipment")
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "purchaseshipment")
public class PurchaseShipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

    @ManyToOne
    private ShipmentItem shipmentItem;

    @ManyToOne(fetch = FetchType.LAZY)
    private PurchaseItem purchaseItem;

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

    public PurchaseShipment quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public ShipmentItem getShipmentItem() {
        return shipmentItem;
    }

    public PurchaseShipment shipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItem = shipmentItem;
        return this;
    }

    public void setShipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItem = shipmentItem;
    }

    public PurchaseItem getPurchaseItem() {
        return purchaseItem;
    }

    public PurchaseShipment purchaseItem(PurchaseItem purchaseItem) {
        this.purchaseItem = purchaseItem;
        return this;
    }

    public void setPurchaseItem(PurchaseItem purchaseItem) {
        this.purchaseItem = purchaseItem;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseShipment)) {
            return false;
        }
        return id != null && id.equals(((PurchaseShipment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PurchaseShipment{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
