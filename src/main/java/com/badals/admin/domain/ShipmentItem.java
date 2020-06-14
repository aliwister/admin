package com.badals.admin.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A ShipmentItem.
 */
@Entity
@Table(name = "shipment_item")
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "shipmentitem")
public class ShipmentItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("shipmentItems")
    private Shipment shipment;
//
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", updatable = false, insertable = false, referencedColumnName = "ref")
    private Product product;

    @Column(name="product_id")
    private Long productId;

    @Column(name="_from")
    private Long from;

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    @OneToMany(mappedBy = "shipmentItem", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<PurchaseShipment> purchaseShipments = new HashSet<>();

    public Set<PurchaseShipment> getPurchaseShipments() {
        return purchaseShipments;
    }

    public void setPurchaseShipments(Set<PurchaseShipment> purchaseShipments) {
        this.purchaseShipments = purchaseShipments;
    }

    public ShipmentItem addPurchaseShipment(PurchaseShipment purchaseShipment) {
        this.purchaseShipments.add(purchaseShipment);
        purchaseShipment.setShipmentItem(this);
        return this;
    }

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

    public Integer getSequence() {
        return sequence;
    }

    public ShipmentItem sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public ShipmentItem quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public ShipmentItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public ShipmentItem shipment(Shipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }


    public Product getProduct() {
        return product;
    }

    public ShipmentItem product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentItem)) {
            return false;
        }
        return id != null && id.equals(((ShipmentItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShipmentItem{" +
            "id=" + getId() +
            ", sequence=" + getSequence() +
            ", quantity=" + getQuantity() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
