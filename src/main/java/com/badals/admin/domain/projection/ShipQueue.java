package com.badals.admin.domain.projection;

import java.math.BigDecimal;

public interface ShipQueue {
   Long getId();
   String getFullName();
   String getReference();
   BigDecimal getTotal();
   BigDecimal getDone();
   BigDecimal getTodo();
   String getCarrier();
   String getEstimatedShipDate();
}
