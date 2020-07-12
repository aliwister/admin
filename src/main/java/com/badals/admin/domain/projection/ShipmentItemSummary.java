package com.badals.admin.domain.projection;

import lombok.Data;

public interface ShipmentItemSummary {
    Integer getId();
    String getTrackingNum();
    Long getTotal();
    String getStatus();
    Long getProcessed();
}
