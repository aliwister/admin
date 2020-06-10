package com.badals.admin.domain.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetrackDeliveryV2 {

   String date;
   @JsonProperty(value = "do_number")
   String _do;

   String type;

   String address;
   ArrayList<DetrackItemV2> items;
   public String getPhone() {
      return phone_number;
   }

   public String phone_number;
   public String instructions;
   public String deliver_to_collect_from;
   public String delivery_time;

   public String assign_to;
   public String notify_email;
   public String webhook_url;
   public String attachment_url;
   public String address_lat;
   public String address_lng;

   String sales_person;

   public Float payment_amount;

   //String view_image_url;
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

