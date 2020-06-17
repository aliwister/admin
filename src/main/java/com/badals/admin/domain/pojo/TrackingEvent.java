package com.badals.admin.domain.pojo;

import lombok.Data;

@Data
public class TrackingEvent {
    Integer id;
    String name;

    public TrackingEvent(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
