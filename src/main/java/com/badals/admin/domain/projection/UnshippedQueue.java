package com.badals.admin.domain.projection;

import org.joda.time.LocalDate;

import java.math.BigDecimal;

public interface UnshippedQueue {
   Long getId();
   String getDescription();
   BigDecimal getQuantity();
   String getDate();
   BigDecimal getPrice();
   String getImage();
   BigDecimal getWeight();
   String getSku();
   Long getPo();
}
