package com.badals.admin.domain.pojo;

import com.badals.admin.domain.projection.ShipmentItemSummary;
import io.netty.handler.codec.http.HttpResponse;
import lombok.Data;

@Data
public class ShipmentItemSummaryImpl implements ShipmentItemSummary {
    public ShipmentItemSummaryImpl(String trackingNum, Long total) {
        this.trackingNum = trackingNum;
        this.total = total;
    }
    Integer id;
    String trackingNum;
    Long total;
    String status;
    Long processed;

    public ShipmentItemSummaryImpl id(Integer id) {
        this.id = id;
        return this;
    }

    public ShipmentItemSummaryImpl total(Long total) {
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
}
