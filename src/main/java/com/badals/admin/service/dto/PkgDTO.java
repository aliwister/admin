package com.badals.admin.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import com.badals.admin.domain.enumeration.PackageType;
import lombok.Data;

/**
 * A DTO for the {@link com.badals.admin.domain.Pkg} entity.
 */
@Data
public class PkgDTO implements Serializable {

    private Long id;

    private BigDecimal length;

    private BigDecimal width;

    private BigDecimal height;

    private BigDecimal weight;

    private PackageType packageType;

    private Set<ShipmentItemDTO> shipmentItems;

    private Long shipmentId;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PkgDTO pkgDTO = (PkgDTO) o;
        if (pkgDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pkgDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PkgDTO{" +
            "id=" + getId() +
            ", length=" + getLength() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", weight=" + getWeight() +
            ", packageType='" + getPackageType() + "'" +
            ", shipment=" + getShipmentId() +
            "}";
    }
}
