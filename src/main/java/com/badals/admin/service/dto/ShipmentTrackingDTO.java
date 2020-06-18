package com.badals.admin.service.dto;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.admin.domain.ShipmentTracking} entity.
 */
@Data
public class ShipmentTrackingDTO implements Serializable {

    private Long id;

    private String status;

    private String details;


    private Long shipmentId;

    private LocalDateTime eventDate;
    private Long shipmentEventId;
    private String shipmentEventDescription;

    private Date createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShipmentTrackingDTO shipmentStatusDTO = (ShipmentTrackingDTO) o;
        if (shipmentStatusDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shipmentStatusDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
