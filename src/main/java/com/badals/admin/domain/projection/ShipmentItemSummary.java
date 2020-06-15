package com.badals.admin.domain.projection;

import lombok.Data;

public interface ShipmentItemSummary {
    String getTrackingNum();
    Long getTotal();
    String getStatus();
    Long getProcessed();
}
