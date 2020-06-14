package com.badals.admin.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import com.badals.admin.domain.enumeration.RejectReason;

/**
 * A ShipmentReceipt.
 */
@Entity
@Table(name = "shipment_receipt")
public class ShipmentReceipt extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "received_date")
    private Instant receivedDate;

    @Column(name = "accepted", precision = 21, scale = 2)
    private BigDecimal accepted;

    @Column(name = "rejected", precision = 21, scale = 2)
    private BigDecimal rejected;

    @Column
    private String sku;


    @Enumerated(EnumType.STRING)
    @Column(name = "reject_reason")
    private RejectReason rejectReason;

    @ManyToOne
    @JsonIgnoreProperties("shipmentReceipts")
    private Pkg pkg;

    @ManyToOne
    @JsonIgnoreProperties("shipmentReceipts")
    private ShipmentItem shipmentItem;

 /*   @ManyToOne
    @JsonIgnoreProperties("shipmentReceipts")
    private Product product;*/

    @Column(name="product_id")
    private Long productId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReceivedDate() {
        return receivedDate;
    }

    public ShipmentReceipt receivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
        return this;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public BigDecimal getAccepted() {
        return accepted;
    }

    public ShipmentReceipt accepted(BigDecimal accepted) {
        this.accepted = accepted;
        return this;
    }

    public void setAccepted(BigDecimal accepted) {
        this.accepted = accepted;
    }

    public BigDecimal getRejected() {
        return rejected;
    }

    public ShipmentReceipt rejected(BigDecimal rejected) {
        this.rejected = rejected;
        return this;
    }

    public void setRejected(BigDecimal rejected) {
        this.rejected = rejected;
    }

    public RejectReason getRejectReason() {
        return rejectReason;
    }

    public ShipmentReceipt rejectReason(RejectReason rejectReason) {
        this.rejectReason = rejectReason;
        return this;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public ShipmentReceipt sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setRejectReason(RejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Pkg getPkg() {
        return pkg;
    }

    public ShipmentReceipt pkg(Pkg pkg) {
        this.pkg = pkg;
        return this;
    }

    public void setPkg(Pkg pkg) {
        this.pkg = pkg;
    }

    public ShipmentItem getShipmentItem() {
        return shipmentItem;
    }

    public ShipmentReceipt shipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItem = shipmentItem;
        return this;
    }

    public void setShipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItem = shipmentItem;
    }

/*    public Product getProduct() {
        return product;
    }

    public ShipmentReceipt product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }*/
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentReceipt)) {
            return false;
        }
        return id != null && id.equals(((ShipmentReceipt) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShipmentReceipt{" +
            "id=" + getId() +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", accepted=" + getAccepted() +
            ", rejected=" + getRejected() +
            ", rejectReason='" + getRejectReason() + "'" +
            "}";
    }
}
