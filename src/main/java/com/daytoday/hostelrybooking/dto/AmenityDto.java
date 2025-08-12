package com.daytoday.hostelrybooking.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class AmenityDto {
    private UUID id;
    private String category;
    private String name;
}
