package com.badals.admin.domain.projection;

import java.math.BigDecimal;

public interface UnshippedQueue {
   Long getId();
   String getDescription();
   BigDecimal getQuantity();
   BigDecimal getDate();
   BigDecimal getPrice();
   String getImage();
   BigDecimal getWeight();
   String getSku();
   Long getPo();
}
