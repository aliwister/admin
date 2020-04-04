package com.badals.admin.service.dto;
import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import com.badals.admin.domain.enumeration.RejectReason;

/**
 * A DTO for the {@link com.badals.admin.domain.ShipmentReceipt} entity.
 */
public class ShipmentReceiptDTO implements Serializable {

    private Long id;

    private Instant receivedDate;

    private BigDecimal accepted;

    private BigDecimal rejected;

    private RejectReason rejectReason;


    private Long pkgId;

    private Long shipmentItemId;

    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Instant receivedDate) {
        this.receivedDate = receivedDate;
    }

    public BigDecimal getAccepted() {
        return accepted;
    }

    public void setAccepted(BigDecimal accepted) {
        this.accepted = accepted;
    }

    public BigDecimal getRejected() {
        return rejected;
    }

    public void setRejected(BigDecimal rejected) {
        this.rejected = rejected;
    }

    public RejectReason getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(RejectReason rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Long getPkgId() {
        return pkgId;
    }

    public void setPkgId(Long pkgId) {
        this.pkgId = pkgId;
    }

    public Long getShipmentItemId() {
        return shipmentItemId;
    }

    public void setShipmentItemId(Long shipmentItemId) {
        this.shipmentItemId = shipmentItemId;
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

        ShipmentReceiptDTO shipmentReceiptDTO = (ShipmentReceiptDTO) o;
        if (shipmentReceiptDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shipmentReceiptDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShipmentReceiptDTO{" +
            "id=" + getId() +
            ", receivedDate='" + getReceivedDate() + "'" +
            ", accepted=" + getAccepted() +
            ", rejected=" + getRejected() +
            ", rejectReason='" + getRejectReason() + "'" +
            ", pkg=" + getPkgId() +
            ", shipmentItem=" + getShipmentItemId() +
            ", product=" + getProductId() +
            "}";
    }
}
