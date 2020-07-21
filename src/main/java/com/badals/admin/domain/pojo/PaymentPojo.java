package com.badals.admin.domain.pojo;

import lombok.Data;

@Data
public class PaymentPojo {
    Price price;
    Long userId;
    String invoiceNum;
}
