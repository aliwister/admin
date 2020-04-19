package com.badals.admin.domain.projection;

import java.math.BigDecimal;

public interface Inventory {
   Long getProductId();
   String getTitle();
   BigDecimal getQuantityOnHand();
   BigDecimal getReceived();
   BigDecimal getIssued();
   String getSku();
   String getImage();
}
