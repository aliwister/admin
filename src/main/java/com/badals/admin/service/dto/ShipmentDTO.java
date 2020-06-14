package com.badals.admin.service.dto;
import java.time.LocalDate;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

import com.badals.admin.domain.enumeration.ShipmentType;
import com.badals.admin.domain.enumeration.ShipmentStatus;
import lombok.Data;

/**
 * A DTO for the {@link com.badals.admin.domain.Shipment} entity.
 */
@Data
@org.springframework.data.elasticsearch.annotations.Document(indexName = "shipment")
public class ShipmentDTO implements Serializable {

    private Long id;

    private LocalDate estimatedShipDate;

    private LocalDate estimatedReadyDate;

    private LocalDate estimatedArrivalDate;

    private BigDecimal estimatedShipCost;

    private BigDecimal actualShipCost;

    private LocalDate latestCancelDate;

    private String handlingInstructions;

    private String reference;

    private String trackingNum;

    private String trackingLink;

    private String shipmentMethod;

    private ShipmentType shipmentType;

    private ShipmentStatus shipmentStatus;


    private Long addressId;

    private Long customerId;

    private String customerFirstName;
    private String customerLastName;
    private String customerCity;
    private String merchantName;
    private Long merchantId;

    private Set<PkgDTO> pkgs;
    private Set<ShipmentItemDTO> shipmentItems;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShipmentDTO shipmentDTO = (ShipmentDTO) o;
        if (shipmentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shipmentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShipmentDTO{" +
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
            ", address=" + getAddressId() +
            "}";
    }
}
