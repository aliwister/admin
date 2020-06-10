package com.badals.admin.domain.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetrackItemV2 {
   Long sku;
   String description;
   Integer quantity;
}
