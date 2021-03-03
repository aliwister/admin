package com.badals.admin.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetrackItem {
   Long sku;
   String description;
   Integer quantity;
}
