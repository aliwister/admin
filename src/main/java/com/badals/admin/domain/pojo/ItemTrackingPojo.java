package com.badals.admin.domain.pojo;
import com.badals.admin.domain.projection.ItemTracking;
import com.badals.admin.service.dto.ShipmentDocDTO;
import com.badals.admin.service.dto.ShipmentTrackingDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class ItemTrackingPojo {
    Long id;
    String description;
    String image;
    BigDecimal quantity;
    Long reference;
    Long po;

    Date orderDate;
    Date invoiceDate;
    Date purchaseDate;

    List<ShipmentPojo> purchaseShipments;
    List<ShipmentPojo> transitShipments;
    List<ShipmentPojo> customerShipments;

    BigDecimal delivered;

    public ItemTrackingPojo(ItemTracking i) {
        this(i.getId(), i.getDescription(), i.getImage(), i.getQuantity(), i.getReference(), i.getPo(), i.getOrderDate(), i.getInvoiceDate(), i.getPurchaseDate(), i.getDelivered());
        if (i.getPurchaseShipments() != null) {
            purchaseShipments = new ArrayList<>();
            initializeShipment(purchaseShipments, i.getPurchaseShipments());
        }
       if (i.getTransitShipments() != null) {
            transitShipments = new ArrayList<>();
            initializeShipment(transitShipments, i.getTransitShipments());
        }
       if (i.getCustomerShipments() != null) {
            customerShipments = new ArrayList<>();
            initializeShipment(customerShipments, i.getCustomerShipments());
        }
    }

    private void initializeShipment(List<ShipmentPojo> target, String purchaseShipments) {
        String s[] = purchaseShipments.split(",");
        for (String x: s) {
            String s2[] = x.split(":");
            if(s2.length == 4) {
                target.add(new ShipmentPojo(s2[0],s2[1],s2[2],s2[3]));
            }
        }
    }

    public ItemTrackingPojo(Long id, String description, String image, BigDecimal quantity, Long reference, Long po, Date orderDate, Date invoiceDate, Date purchaseDate, BigDecimal delivered) {
        this.id = id;
        this.description = description;
        this.image = image;
        this.quantity = quantity;
        this.reference = reference;
        this.po = po;
        this.orderDate = orderDate;
        this.invoiceDate = invoiceDate;
        this.purchaseDate = purchaseDate;
        this.delivered = delivered;
    }
}
