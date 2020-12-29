package com.badals.admin.domain.pojo;

import com.badals.admin.domain.projection.ShipmentItemSummary;
import io.netty.handler.codec.http.HttpResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShipmentItemSummaryImpl implements ShipmentItemSummary {
    public ShipmentItemSummaryImpl(String trackingNum, BigDecimal total) {
        this.trackingNum = trackingNum;
        this.total = total;
    }
    Integer id;
    String trackingNum;
    BigDecimal total;
    String status;
    Long processed;
    String reference;

    public ShipmentItemSummaryImpl id(Integer id) {
        this.id = id;
        return this;
    }

    public ShipmentItemSummaryImpl total(BigDecimal total) {
        this.total = total;
        return this;
    }
    public ShipmentItemSummaryImpl processed(Long processed) {
        this.processed = processed;
        return this;
    }

    public ShipmentItemSummaryImpl status(String status) {
        this.status = status;
        return this;
    }

    public ShipmentItemSummaryImpl reference(String reference) {
        this.reference = reference;
        return this;
    }
}
