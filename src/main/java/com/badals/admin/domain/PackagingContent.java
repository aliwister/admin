package com.badals.admin.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A PackagingContent.
 */
@Entity
@Table(name = "packaging_content")
public class PackagingContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

    @ManyToOne
    @JsonIgnoreProperties("packagingContents")
    private Pkg pkg;

    @ManyToOne
    @JsonIgnoreProperties("packagingContents")
    private ShipmentItem shipmentItem;

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

    public PackagingContent quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Pkg getPkg() {
        return pkg;
    }

    public PackagingContent pkg(Pkg pkg) {
        this.pkg = pkg;
        return this;
    }

    public void setPkg(Pkg pkg) {
        this.pkg = pkg;
    }

    public ShipmentItem getShipmentItem() {
        return shipmentItem;
    }

    public PackagingContent shipmentItem(ShipmentItem shipmentItem) {
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
        if (!(o instanceof PackagingContent)) {
            return false;
        }
        return id != null && id.equals(((PackagingContent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PackagingContent{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
