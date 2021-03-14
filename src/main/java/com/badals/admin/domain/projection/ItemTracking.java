package com.badals.admin.domain.projection;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

public interface ItemTracking {
    Long getId();
    String getDescription();
    String getImage();
    BigDecimal getQuantity();
    Long getReference();
    Long getPo();

    Date getOrderDate();
    Date getInvoiceDate();
    Date getPurchaseDate();

    String getPurchaseShipments();
    String getTransitShipments();
    String getCustomerShipments();

    String getMerchant();
    Long getMerchantId();
    String getSku();
    String getUrl();

    BigDecimal getDelivered();
}
