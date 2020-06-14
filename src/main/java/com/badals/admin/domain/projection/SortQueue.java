package com.badals.admin.domain.projection;

import java.math.BigDecimal;

public interface SortQueue {
   Long getId();
   String getDescription();
   BigDecimal getQuantity();
   BigDecimal getPreallocated();
   BigDecimal getPrice();
   BigDecimal getCost();
   String getImage();
   BigDecimal getWeight();
   String getUrl();
   String getSku();
   String getShippingInstructions();
   Long getOrderId();
   Long getOrderItemId();
   Long getMerchantId();
   Long getProductId();
}
