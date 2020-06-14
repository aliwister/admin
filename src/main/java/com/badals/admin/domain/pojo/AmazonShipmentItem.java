package com.badals.admin.domain.pojo;

import com.univocity.parsers.annotations.Parsed;
import com.univocity.parsers.annotations.Replace;
import lombok.Data;

@Data
public class AmazonShipmentItem {

    @Parsed(field = "PO Number")
    @Replace(expression = "[=\"]", replacement = "")
    private String po;

    @Parsed(field = "Order Quantity")
    private String orderQuantity;

    @Parsed(field = "Order ID")
    private String orderId;

    @Parsed(field = "Order Status")
    private String orderStatus;

    @Parsed(field = "Shipment Date")
    private String shipmentDate;

    @Parsed(field = "Shipment Status")
    private String shipmentStatus;

    @Parsed(field = "Carrier Tracking #")
    @Replace(expression = "[=\"]", replacement = "")
    private String trackingNum;

    @Parsed(field = "Item Quantity")
    private String quantity;

    @Parsed(field = "Shipping Address")
    private String shippingAddress;

    @Parsed(field = "Carrier Name")
    private String carrier;

    @Parsed(field = "Product Category")
    private String category;

    @Parsed(field = "ASIN")
    @Replace(expression = "[=\"]", replacement = "")
    private String asin;

    @Parsed(field = "Title")
    private String title;

    @Parsed(field = "Brand")
    private String brand;

    @Parsed(field = "Purchase PPU")
    private String price;
}
