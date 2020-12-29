package com.badals.admin.domain.projection;

import lombok.Data;

import java.math.BigDecimal;

public interface ShipmentItemSummary {
    Integer getId();
    String getTrackingNum();
    BigDecimal getTotal();
    String getStatus();
    Long getProcessed();
    String getReference();
}
