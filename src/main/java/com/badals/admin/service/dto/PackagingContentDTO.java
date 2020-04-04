package com.badals.admin.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.admin.domain.PackagingContent} entity.
 */
public class PackagingContentDTO implements Serializable {

    private Long id;

    private BigDecimal quantity;


    private Long pkgId;

    private Long shipmentItemId;

    public PackagingContentDTO(BigDecimal quantity, Long pkgId, Long shipmentItemId) {
        this.quantity = quantity;
        this.pkgId = pkgId;
        this.shipmentItemId = shipmentItemId;
    }

    public PackagingContentDTO() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PackagingContentDTO packagingContentDTO = (PackagingContentDTO) o;
        if (packagingContentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), packagingContentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PackagingContentDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", pkg=" + getPkgId() +
            ", shipmentItem=" + getShipmentItemId() +
            "}";
    }
}
