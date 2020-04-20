package com.badals.admin.domain.projection;

import java.math.BigDecimal;

public interface OutstandingQueue {
   Long getId();
   String getDescription();
   BigDecimal getQuantity();
   BigDecimal getAllocated();
   BigDecimal getPrice();
   String getImage();
   BigDecimal getWeight();
   String getSku();
   Long getOrderId();
   Long getOrderItemId();
   Long getProductId();
}
