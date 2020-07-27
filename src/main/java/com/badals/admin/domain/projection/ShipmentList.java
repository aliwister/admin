package com.badals.admin.domain.projection;

public interface ShipmentList {
    Integer getId();
    String getCreatedDate();
    String getShipmentMethod();
    String getTrackingNum();
    String getStatus();
    Integer getPkgCount();
    Integer getArrivedPkgs();
}
