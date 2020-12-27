package com.badals.admin.domain;
import com.badals.admin.domain.pojo.PaymentPojo;
import com.badals.admin.domain.pojo.Price;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Type;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.badals.admin.domain.enumeration.ShipmentType;

import com.badals.admin.domain.enumeration.ShipmentStatus;

/**
 * A Shipment.
 */
@Entity
@Table(name = "shipment")
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "shipment")
public class Shipment extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "estimated_ship_date")
    private Date estimatedShipDate;

    public LocalDateTime getActualShipDate() {
        return actualShipDate;
    }

    public void setActualShipDate(LocalDateTime actualShipDate) {
        this.actualShipDate = actualShipDate;
    }

    @Column(name = "actual_ship_date")
    private LocalDateTime actualShipDate;

    @Column(name = "estimated_ready_date")
    private LocalDate estimatedReadyDate;

    @Column(name = "estimated_arrival_date")
    private LocalDate estimatedArrivalDate;

    @Column(name = "estimated_ship_cost", precision = 21, scale = 2)
    private BigDecimal estimatedShipCost;

    @Column(name = "actual_ship_cost", precision = 21, scale = 2)
    private BigDecimal actualShipCost;

    @Column(name = "latest_cancel_date")
    private LocalDate latestCancelDate;

    @Column(name = "handling_instructions")
    private String handlingInstructions;

    @Column(name = "reference")
    private String reference;

    @Column(name = "tracking_num")
    private String trackingNum;

    @Column(name = "tracking_link")
    private String trackingLink;

    @Column(name = "shipment_method")
    private String shipmentMethod;

    @Column(name = "pkg_count")
    private Integer pkgCount;

    public Integer getPkgCount() {
        return pkgCount;
    }

    public void setPkgCount(Integer pkgCount) {
        this.pkgCount = pkgCount;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_type")
    private ShipmentType shipmentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status")
    private ShipmentStatus shipmentStatus;

    @ManyToOne
    @JsonIgnoreProperties("shipments")
    @JoinColumn(name = "address_id",referencedColumnName = "id_address")
    private Address address;

    @OneToMany(mappedBy = "shipment", cascade=CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sequence")
    private Set<ShipmentItem> shipmentItems = new HashSet<>();

    @OneToMany(mappedBy = "shipment", cascade=CascadeType.ALL, orphanRemoval = true)
    @OrderBy("eventDate")
    private Set<ShipmentTracking> shipmentTrackings = new HashSet<>();

    public Set<ShipmentTracking> getShipmentTrackings() {
        return shipmentTrackings;
    }

    public void setShipmentTrackings(Set<ShipmentTracking> shipmentTrackings) {
        this.shipmentTrackings = shipmentTrackings;
    }

    @OneToMany(mappedBy = "shipment")
    private Set<Pkg> pkgs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("shipments")
    @JoinColumn(name = "customer_id",referencedColumnName = "id_customer")
    private Customer customer;

    @ManyToOne
    @JsonIgnoreProperties("shipments")
    private Merchant merchant;

    @Type(type = "json")
    @Column(name = "duties_total", columnDefinition = "string")
    private Price dutiesTotal;

    public Price getDutiesTotal() {
        return dutiesTotal;
    }

    public void setDutiesTotal(Price dutiesTotal) {
        this.dutiesTotal = dutiesTotal;
    }

    /*    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "reference", referencedColumnName = "reference",insertable= false, updatable = false)
    private ShipmentProgress shipmentProgress;

    public ShipmentProgress getShipmentProgress() {
        return shipmentProgress;
    }

    public void setShipmentProgress(ShipmentProgress shipmentProgress) {
        this.shipmentProgress = shipmentProgress;
    }*/

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Pkg> getPkgs() {
        return pkgs;
    }

    public void setPkgs(Set<Pkg> pkgs) {
        this.pkgs = pkgs;
    }

    public Date getEstimatedShipDate() {
        return estimatedShipDate;
    }

    public Shipment estimatedShipDate(Date estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
        return this;
    }

    public void setEstimatedShipDate(Date estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
    }

    public LocalDate getEstimatedReadyDate() {
        return estimatedReadyDate;
    }

    public Shipment estimatedReadyDate(LocalDate estimatedReadyDate) {
        this.estimatedReadyDate = estimatedReadyDate;
        return this;
    }

    public void setEstimatedReadyDate(LocalDate estimatedReadyDate) {
        this.estimatedReadyDate = estimatedReadyDate;
    }

    public LocalDate getEstimatedArrivalDate() {
        return estimatedArrivalDate;
    }

    public Shipment estimatedArrivalDate(LocalDate estimatedArrivalDate) {
        this.estimatedArrivalDate = estimatedArrivalDate;
        return this;
    }

    public void setEstimatedArrivalDate(LocalDate estimatedArrivalDate) {
        this.estimatedArrivalDate = estimatedArrivalDate;
    }

    public BigDecimal getEstimatedShipCost() {
        return estimatedShipCost;
    }

    public Shipment estimatedShipCost(BigDecimal estimatedShipCost) {
        this.estimatedShipCost = estimatedShipCost;
        return this;
    }

    public void setEstimatedShipCost(BigDecimal estimatedShipCost) {
        this.estimatedShipCost = estimatedShipCost;
    }

    public BigDecimal getActualShipCost() {
        return actualShipCost;
    }

    public Shipment actualShipCost(BigDecimal actualShipCost) {
        this.actualShipCost = actualShipCost;
        return this;
    }

    public void setActualShipCost(BigDecimal actualShipCost) {
        this.actualShipCost = actualShipCost;
    }

    public LocalDate getLatestCancelDate() {
        return latestCancelDate;
    }

    public Shipment latestCancelDate(LocalDate latestCancelDate) {
        this.latestCancelDate = latestCancelDate;
        return this;
    }

    public void setLatestCancelDate(LocalDate latestCancelDate) {
        this.latestCancelDate = latestCancelDate;
    }

    public String getHandlingInstructions() {
        return handlingInstructions;
    }

    public Shipment handlingInstructions(String handlingInstructions) {
        this.handlingInstructions = handlingInstructions;
        return this;
    }

    public void setHandlingInstructions(String handlingInstructions) {
        this.handlingInstructions = handlingInstructions;
    }

    public String getReference() {
        return reference;
    }

    public Shipment reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTrackingNum() {
        return trackingNum;
    }

    public Shipment trackingNum(String trackingNum) {
        this.trackingNum = trackingNum;
        return this;
    }

    public void setTrackingNum(String trackingNum) {
        this.trackingNum = trackingNum;
    }

    public String getTrackingLink() {
        return trackingLink;
    }

    public Shipment trackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
        return this;
    }

    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }

    public String getShipmentMethod() {
        return shipmentMethod;
    }

    public Shipment shipmentMethod(String shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
        return this;
    }

    public void setShipmentMethod(String shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
    }

    public ShipmentType getShipmentType() {
        return shipmentType;
    }

    public Shipment shipmentType(ShipmentType shipmentType) {
        this.shipmentType = shipmentType;
        return this;
    }

    public void setShipmentType(ShipmentType shipmentType) {
        this.shipmentType = shipmentType;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public Shipment shipmentStatus(ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
        return this;
    }

    public void setShipmentStatus(ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public Address getAddress() {
        return address;
    }

    public Shipment address(Address address) {
        this.address = address;
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<ShipmentItem> getShipmentItems() {
        return shipmentItems;
    }

    public Shipment shipmentItems(Set<ShipmentItem> shipmentItems) {
        this.shipmentItems = shipmentItems;
        return this;
    }

    public Shipment addShipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItems.add(shipmentItem);
        shipmentItem.setShipment(this);
        return this;
    }

    public Shipment addShipmentTracking(ShipmentTracking t) {
        this.shipmentTrackings.add(t);
        t.setShipment(this);
        return this;
    }

    public Shipment removeShipmentItem(ShipmentItem shipmentItem) {
        this.shipmentItems.remove(shipmentItem);
        shipmentItem.setShipment(null);
        return this;
    }

    public void setShipmentItems(Set<ShipmentItem> shipmentItems) {
        this.shipmentItems = shipmentItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Shipment customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Shipment merchant(Merchant merchant) {
        this.merchant = merchant;
        return this;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shipment)) {
            return false;
        }
        return id != null && id.equals(((Shipment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Shipment{" +
            "id=" + getId() +
            ", estimatedShipDate='" + getEstimatedShipDate() + "'" +
            ", estimatedReadyDate='" + getEstimatedReadyDate() + "'" +
            ", estimatedArrivalDate='" + getEstimatedArrivalDate() + "'" +
            ", estimatedShipCost=" + getEstimatedShipCost() +
            ", actualShipCost=" + getActualShipCost() +
            ", latestCancelDate='" + getLatestCancelDate() + "'" +
            ", handlingInstructions='" + getHandlingInstructions() + "'" +
            ", reference='" + getReference() + "'" +
            ", trackingNum='" + getTrackingNum() + "'" +
            ", trackingLink='" + getTrackingLink() + "'" +
            ", shipmentMethod='" + getShipmentMethod() + "'" +
            ", shipmentType='" + getShipmentType() + "'" +
            ", shipmentStatus='" + getShipmentStatus() + "'" +
            "}";
    }
}
