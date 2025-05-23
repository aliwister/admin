package com.badals.admin.domain.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetrackItem {
   Long sku;
   String desc;
   BigDecimal qty;
}
