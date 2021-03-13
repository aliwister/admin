package com.badals.admin.domain.pojo;

import lombok.Data;

@Data
public class ShipmentPojo {
   String id;
   String trackingNum;
   String shipmentMethod;
   String status;

   public ShipmentPojo(String id, String trackingNum, String shipmentMethod, String status) {
      this.id = id;
      this.trackingNum = trackingNum;
      this.shipmentMethod = shipmentMethod;
      this.status = status;
   }
}
