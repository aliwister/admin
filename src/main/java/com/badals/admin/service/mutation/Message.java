package com.badals.admin.service.mutation;

import lombok.Data;

@Data
public class Message {
    public Message(String value) {
        this.value = value;
    }

    String value;
}
