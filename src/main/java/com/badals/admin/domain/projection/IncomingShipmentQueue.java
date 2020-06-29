package com.badals.admin.domain.projection;

public interface IncomingShipmentQueue {
    Integer getId();
    String getCreatedDate();
    String getShipmentMethod();
    String getTrackingNum();
    Integer getPkgCount();
    Integer getArrivedPkgs();
    String getStatus();
}
