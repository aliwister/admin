package com.badals.admin.domain.pojo;

import lombok.Data;

@Data
public class ItemPojo {

    public ItemPojo(String description, String image, Long quantity) {
        this.description = description;
        this.image = image;
        this.quantity = quantity;
    }

    String description;
    String image;
    Long quantity;
}
