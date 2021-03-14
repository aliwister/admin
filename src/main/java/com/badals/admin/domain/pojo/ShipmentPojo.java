package com.badals.admin.domain.pojo;

import lombok.Data;

@Data
public class ShipmentPojo {
   String id;
   String trackingNum;
   String shipmentMethod;
   String status;
   String to;

   public ShipmentPojo(String id, String trackingNum, String shipmentMethod, String status) {
      this.id = id;
      this.trackingNum = trackingNum;
      this.shipmentMethod = shipmentMethod;
      this.status = status;
   }

   public ShipmentPojo(String id, String trackingNum, String shipmentMethod, String status, String to) {
      this.id = id;
      this.trackingNum = trackingNum;
      this.shipmentMethod = shipmentMethod;
      this.status = status;
      this.to = to;
   }
}
