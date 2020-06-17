package com.badals.admin.domain.pojo;

import com.badals.admin.service.dto.ShipmentTrackingDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ShipmentTrackingPojo {
    Long id;
    String status;
    String date;
    String type;
    String trackingNum;
    String carrier;
    List<ItemPojo> content = new ArrayList<>();
    List<ShipmentTrackingDTO> progress = new ArrayList<>();

    public void addItem(ItemPojo item) {
        content.add(item);
    }

    public ShipmentTrackingPojo(Long id, String status, String date, String type, String trackingNum, String carrier, ItemPojo item) {
        this.id = id;
        this.status = status;
        this.date = date;
        this.type = type;
        this.trackingNum = trackingNum;
        this.carrier = carrier;
        this.content = content;
        this.progress = progress;
        addItem(item);
    }

    public void addProgress(ShipmentTrackingDTO x) {
        progress.add(x);
    }
}
