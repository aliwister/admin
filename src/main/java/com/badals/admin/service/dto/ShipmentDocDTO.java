package com.badals.admin.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.admin.domain.ShipmentDoc} entity.
 */
public class ShipmentDocDTO implements Serializable {

    private Long id;

    private String fileKey;


    private Long shipmentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShipmentDocDTO shipmentDocDTO = (ShipmentDocDTO) o;
        if (shipmentDocDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shipmentDocDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShipmentDocDTO{" +
            "id=" + getId() +
            ", fileKey='" + getFileKey() + "'" +
            ", shipment=" + getShipmentId() +
            "}";
    }
}
