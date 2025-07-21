package com.daytoday.hostelrybooking.dto;

import com.daytoday.hostelrybooking.enums.PropertyTypeEnum;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PropertyDto {
    private UUID id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String country;
    private PropertyTypeEnum type;
    private List<ImageDto> images;
}
