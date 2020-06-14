package com.badals.admin.domain.pojo;

import com.badals.admin.domain.projection.ShipmentItemCount;
import lombok.Data;

@Data
public class ShipmentItemCountImpl implements ShipmentItemCount {
    String trackingNum;
    Long count;

    public ShipmentItemCountImpl(String trackingNum, Long count) {
        this.trackingNum = trackingNum;
        this.count = count;
    }
}
