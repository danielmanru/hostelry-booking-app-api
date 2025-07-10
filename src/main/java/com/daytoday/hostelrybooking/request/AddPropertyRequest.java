package com.daytoday.hostelrybooking.request;

import com.daytoday.hostelrybooking.enums.PropertyTypeEnum;
import com.daytoday.hostelrybooking.model.User;
import lombok.Data;

@Data
public class AddPropertyRequest {
    private Long id;
    private User owner;
    private String name;
    private String description;
    private String address;
    private String city;
    private String country;
    private PropertyTypeEnum type;
}
