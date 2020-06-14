package com.badals.admin.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.badals.admin.domain.enumeration.PackageType;

/**
 * A Pkg.
 */
@Entity
@Table(name = "pkg")
public class Pkg extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "length", precision = 21, scale = 2)
    private BigDecimal length;

    @Column(name = "width", precision = 21, scale = 2)
    private BigDecimal width;

    @Column(name = "height", precision = 21, scale = 2)
    private BigDecimal height;

    @Column(name = "weight", precision = 21, scale = 2)
    private BigDecimal weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_type")
    private PackageType packageType;

    @ManyToOne
    @JsonIgnoreProperties("pkgs")
    private Shipment shipment;

    @OneToMany(mappedBy = "pkg")
    Set<PackagingContent> packagingContent;

    public Set<PackagingContent> getPackagingContent() {
        return packagingContent;
    }

    public void setPackagingContent(Set<PackagingContent> packagingContent) {
        this.packagingContent = packagingContent;
    }

    @OneToMany
    @JoinTable(name = "packaging_content",
        joinColumns = @JoinColumn(name = "pkg_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "shipment_item_id", referencedColumnName = "id"))
    @OrderBy("sequence")
    Set<ShipmentItem> shipmentItems;

    public Set<ShipmentItem> getShipmentItems() {
        return shipmentItems;
    }

    public void setShipmentItems(Set<ShipmentItem> shipmentItems) {
        this.shipmentItems = shipmentItems;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getLength() {
        return length;
    }

    public Pkg length(BigDecimal length) {
        this.length = length;
        return this;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public Pkg width(BigDecimal width) {
        this.width = width;
        return this;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public Pkg height(BigDecimal height) {
        this.height = height;
        return this;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public Pkg weight(BigDecimal weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public PackageType getPackageType() {
        return packageType;
    }

    public Pkg packageType(PackageType packageType) {
        this.packageType = packageType;
        return this;
    }

    public void setPackageType(PackageType packageType) {
        this.packageType = packageType;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public Pkg shipment(Shipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pkg)) {
            return false;
        }
        return id != null && id.equals(((Pkg) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Pkg{" +
            "id=" + getId() +
            ", length=" + getLength() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", weight=" + getWeight() +
            ", packageType='" + getPackageType() + "'" +
            "}";
    }
}
