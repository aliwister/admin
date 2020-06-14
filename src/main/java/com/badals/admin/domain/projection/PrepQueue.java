package com.badals.admin.domain.projection;

import java.math.BigDecimal;

public interface PrepQueue {
   Long getId();
   Integer getSequence();
   BigDecimal getQuantity();
   String getDescription();

   BigDecimal getUnpacked();
   String getImage();
   Long getShipmentId();
   Long getProductId();
   Long getOrderItemId();
   Long getOrderId();
}
