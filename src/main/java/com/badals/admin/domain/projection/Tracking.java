package com.badals.admin.domain.projection;

import java.time.LocalDate;

public interface Tracking {
    Long getId();
    String getDescription();
    String getImage();
    Long getQuantity();
    String getStatus();
    String getType();
    String getDate();
    String getTrackingNum();
    String getCarrier();
}
