package com.daytoday.hostelrybooking.dto;

import com.daytoday.hostelrybooking.enums.PropertyTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class PropertyDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String country;
    private PropertyTypeEnum type;
    private List<ImageDto> images;
}
