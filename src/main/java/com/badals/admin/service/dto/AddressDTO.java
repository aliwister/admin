package com.badals.admin.service.dto;

import com.badals.admin.domain.pojo.AddressPojo;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String line1;

    private String line2;

    private String city;

    private String mobile;

    private String active;

    private String lat;
    private String lng;

    private Long customerId;

    public AddressDTO() {
    }

    public AddressDTO(String firstName, String lastName, String line1, String line2, String city, String mobile, String lat, String lng) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;

        this.mobile = mobile;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", city='" + city + '\'' +
                ", mobile='" + mobile + '\'' +
                ", active='" + active + '\'' +
                ", customerId=" + customerId +
                '}';
    }

    public static AddressDTO fromAddressPojo(AddressPojo pojo) {
        if(pojo == null)
            return null;
        return new AddressDTO(pojo.getFirstName(),pojo.getLastName(),pojo.getLine1(),pojo.getLine2(),pojo.getCity(), pojo.getMobile(), pojo.getLat(), pojo.getLng());
    }
}
