package com.badals.admin.domain.pojo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShipmentTrackingMap {
    Long id;
    ShipmentTrackingPojo shipment;

    public ShipmentTrackingMap(Long id, ShipmentTrackingPojo shipment) {
        this.id = id;
        this.shipment = shipment;
    }
}
