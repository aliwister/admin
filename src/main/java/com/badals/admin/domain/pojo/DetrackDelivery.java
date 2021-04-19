package com.badals.admin.domain.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetrackDelivery {

   String date;
   @JsonProperty(value = "do")
   String _do;
   String address;
   ArrayList<DetrackItem> items;
   public String getPhone() {
      return phone;
   }

   public String phone;
   public String instructions;
   public String deliver_to;
   public String delivery_time;

   public String assign_to;
   public String notify_email;
   public String notify_url;
   public String address_lat;
   public String address_lng;
   public String att_1;
   public String city;

   String sales_person;

   public String pay_amt;

   String view_image_url;
   String view_signature_url;
   String view_photo_1_url;
   String view_photo_2_url;
   String view_photo_3_url;
   Double pod_lat;
   Double pod_lng;
   String paid;
   String status;
   String reason;
}

