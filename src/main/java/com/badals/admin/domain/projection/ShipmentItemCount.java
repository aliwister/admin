package com.badals.admin.domain.projection;

import lombok.Data;

public interface ShipmentItemCount {
    String getTrackingNum();
    Long getCount();
}
