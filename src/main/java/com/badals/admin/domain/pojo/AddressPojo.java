package com.badals.admin.domain.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressPojo implements Serializable {
    Long id;
    String firstName;
    String lastName;
    String line1;
    String line2;
    String city;
    String state;
    String country;
    String postalCode;
    String mobile;
    Boolean save;
    String alias;
    String plusCode;
    String lat;
    String lng;
}
